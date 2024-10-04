package com.example.myapplication.moduel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.repo.volunter_repo
import kotlinx.coroutines.launch
import java.io.InputStream

class volunter_viewmodel(private val repository: volunter_repo) : ViewModel(){
  private val _students = MutableLiveData<List<volunter_structure>>()
  val students: LiveData<List<volunter_structure>> = _students

  private val _errorMessage = MutableLiveData<String>()
  val errorMessage: LiveData<String> = _errorMessage

  fun loadStudentsFromExcel(inputStream: InputStream) {
    viewModelScope.launch {
      val (studentList, error) = repository.getStudentsFromExcel(inputStream)
      if (error.isNotEmpty()) {
        _errorMessage.value = error
      } else {
        _students.value = studentList
        Log.d("volunter_viewmodel", "LiveData updated with ${_students.value?.size} students")
      }
    }
  }

}