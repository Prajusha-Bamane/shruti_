package com.example.myapplication.moduel

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.repo.volunter_repo
import kotlinx.coroutines.launch
import java.io.InputStream
import java.util.concurrent.atomic.AtomicBoolean
import com.google.firebase.firestore.FirebaseFirestore

class volunter_viewmodel(private val repository: volunter_repo) : ViewModel() {

  private val _students = MutableLiveData<List<volunter_structure>>()
  val students: LiveData<List<volunter_structure>> = _students

  private val _isLoading = SingleLiveEvent<Boolean>()
  val isLoading: LiveData<Boolean> = _isLoading

  private val _errorMessage = SingleLiveEvent<String>()
  val errorMessage: LiveData<String> get() = _errorMessage

  fun uploadExcelAndStoreData(inputStream: InputStream) {
    viewModelScope.launch {
      val db =FirebaseFirestore.getInstance()


      db.collection("students")
        .get()
        .addOnSuccessListener { documents ->
          val batch = db.batch()
          for (document in documents) {
            batch.delete(document.reference)
          }
          batch.commit()
            .addOnSuccessListener {
              // 2. Parse Excel and store new data
              val (studentList, error) = repository.getStudentsFromExcel(inputStream)
              if (error.isNotEmpty()) {
                _errorMessage.value = error
              } else {
                val newBatch = db.batch()
                for (student in studentList) {
                  val docRef = db.collection("students").document(student.enrollmentNo) // Use enrollmentNo as document ID
                  newBatch.set(docRef, student)
                }
                newBatch.commit()
                  .addOnSuccessListener {
                    _students.value = studentList
                    Log.d("volunter_viewmodel", "LiveData updated with ${_students.value?.size} students")
                  }
                  .addOnFailureListener {e ->
                    _errorMessage.value = "Error storing data in Firestore: ${e.message}"
                  }
              }
            }
            .addOnFailureListener { e ->
              _errorMessage.value = "Error deleting data from Firestore: ${e.message}"
            }
        }

        .addOnFailureListener { e ->
          _errorMessage.value = "Error retrieving data from Firestore: ${e.message}"
        }

    }
  }

  fun getStudentsFromFirestore() {
    viewModelScope.launch {
      _isLoading.value = true
      val db = FirebaseFirestore.getInstance()
      db.collection("students")
        .get()
        .addOnSuccessListener { documents ->
          val studentList = documents.map { document ->
            document.toObject(volunter_structure::class.java)
          }
          _students.value = studentList
        }
        .addOnFailureListener { e ->
          _errorMessage.value = "Error retrieving data from Firestore: ${e.message}"
        }
        .addOnCompleteListener {
          _isLoading.value = false // Hide progress bar
        }
    }
  }

  // SingleLiveEvent implementation within the same file
  private class SingleLiveEvent<T> : MutableLiveData<T>() {

    private val pending = AtomicBoolean(false)

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
      if (hasActiveObservers()) {
        Log.w("SingleLiveEvent", "Multiple observers registered but only one will be notified of changes.")
      }

      super.observe(owner) { t ->
        if (pending.compareAndSet(true, false)) {
          observer.onChanged(t)
        }
      }
    }

    override fun setValue(t: T?) {
      pending.set(true)
      super.setValue(t)
    }

    /**
     * Used for cases where T is Void, to make calls cleaner.
     */
    fun call() {
      value = null
    }
  }
}