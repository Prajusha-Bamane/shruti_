package nav_fregment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.adapter_contact
import com.example.myapplication.databinding.FragmentEventBinding
import com.example.myapplication.event_structure


@Suppress()
class event : Fragment() {
lateinit var binding: FragmentEventBinding

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val arratcontact = ArrayList<event_structure>()

        arratcontact.add(event_structure(R.drawable.img_3,"nothing","15-02-2002","nothing"))
        arratcontact.add(event_structure(R.drawable.img_3,"nothing","15-02-2002","nothing"))
        arratcontact.add(event_structure(R.drawable.img_3,"nothing","15-02-2002","nothing"))
        arratcontact.add(event_structure(R.drawable.img_3,"nothing","15-02-2002","nothing"))
        arratcontact.add(event_structure(R.drawable.img_3,"nothing","15-02-2002","nothing"))
        arratcontact.add(event_structure(R.drawable.img_3,"nothing","15-02-2002","nothing"))

        binding.eventRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.eventRecyclerView.adapter = adapter_contact(requireContext(), arratcontact)
    }



}


