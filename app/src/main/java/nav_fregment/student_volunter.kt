package nav_fregment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.adapter.volunter_adapter
import com.example.myapplication.databinding.FragmentStudentVolunterBinding
import com.example.myapplication.moduel.volunter_structure
import com.example.myapplication.moduel.volunter_viewmodel
import com.example.myapplication.repo.volunter_repo // Make sure to import this
import volunter_viewmodelFactory

class student_volunter : Fragment() {

  private lateinit var binding: FragmentStudentVolunterBinding
  private var adapter: volunter_adapter? = null

  private val repository = volunter_repo() // Initialize repository here
  private val viewModel: volunter_viewmodel by activityViewModels {
    volunter_viewmodelFactory(repository)
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = FragmentStudentVolunterBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)


    binding.studentRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    adapter = volunter_adapter(emptyList())
    binding.studentRecyclerView.adapter = adapter

    viewModel.getStudentsFromFirestore()

    viewModel.students.observe(viewLifecycleOwner) { students ->
      if (adapter == null) {
        adapter = volunter_adapter(students)
        binding.studentRecyclerView.adapter = adapter
      } else {
        adapter?.studentdata = students as ArrayList<volunter_structure>
        adapter?.notifyDataSetChanged()
      }
    }
  }
}