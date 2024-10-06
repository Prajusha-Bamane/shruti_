package nav_fregment

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.adapter.adapter_contact
import com.example.myapplication.databinding.FragmentEventBinding
import com.example.myapplication.moduel.event_viewmodel


@Suppress()
class event : Fragment() {
    val adapter = adapter_contact(
        { eventId -> /* Do nothing */ }, false // isEventForMam
    )

    lateinit var binding: FragmentEventBinding
    private lateinit var viewModel: event_viewmodel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentEventBinding.inflate(inflater, container, false)
        return binding.root



    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.eventRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.progressBar.visibility=View.VISIBLE
        binding.eventRecyclerView.adapter = adapter
        viewModel= ViewModelProvider(this).get(event_viewmodel::class.java)

        viewModel.userlist.observe(viewLifecycleOwner, {
            adapter.update_event(it)
            binding.progressBar.visibility=View.GONE
        })






    }


}


