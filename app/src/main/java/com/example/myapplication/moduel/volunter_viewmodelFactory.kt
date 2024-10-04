import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.moduel.volunter_viewmodel
import com.example.myapplication.repo.volunter_repo

class volunter_viewmodelFactory(private val repository: volunter_repo) : ViewModelProvider.Factory {
  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    if (modelClass.isAssignableFrom(volunter_viewmodel::class.java)) {
      @Suppress("UNCHECKED_CAST")
      return volunter_viewmodel(repository) as T
    }
    throw IllegalArgumentException("Unknown ViewModel class")
  }
}