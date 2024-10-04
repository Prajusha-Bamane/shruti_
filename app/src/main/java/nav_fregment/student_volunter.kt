package nav_fregment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentStudentVolunterBinding


class student_volunter : Fragment() {

  private lateinit var binding: FragmentStudentVolunterBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding= FragmentStudentVolunterBinding.inflate(inflater, container, false)
    return binding.root
  }


}