package nav_fregment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.FragmentProfile2Binding


class profile : Fragment() {

    private var _binding: FragmentProfile2Binding? = null
    private val binding get() = _binding!!

    private var isEditMode = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding= FragmentProfile2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.editProfileButton.setOnClickListener {
            if (isEditMode) {
                saveChanges()
                switchToViewMode()
            } else {
                switchToEditMode()
            }
        }

        // Initially, start in view mode
        switchToViewMode()

    }
    private fun switchToEditMode() {
        // Hide TextViews and show EditTexts
        binding.nameTextView.visibility = View.GONE
        binding.emailTextView.visibility = View.GONE
        binding.enrollmentTextView.visibility = View.GONE
        binding.departmentTextView.visibility = View.GONE
        binding.contactTextView.visibility = View.GONE
        binding.addressTextView.visibility = View.GONE

        binding.nameEditText.visibility = View.VISIBLE
        binding.emailEditText.visibility = View.VISIBLE
        binding.enrollmentEditText.visibility = View.VISIBLE
        binding.departmentEditText.visibility = View.VISIBLE
        binding.contactEditText.visibility = View.VISIBLE
        binding.addressEditText.visibility = View.VISIBLE

        // Populate EditTexts with current data
        binding.nameEditText.setText(binding.nameTextView.text.toString())
        binding.emailEditText.setText(binding.emailTextView.text.toString())
        binding.enrollmentEditText.setText(binding.enrollmentTextView.text.toString())
        binding.departmentEditText.setText(binding.departmentTextView.text.toString())
        binding.contactEditText.setText(binding.contactTextView.text.toString())
        binding.addressEditText.setText(binding.addressTextView.text.toString())

        binding.editProfileButton.text = "Save Changes"
        isEditMode = true
    }

    private fun switchToViewMode() {
        // Show TextViews and hide EditTexts
        binding.nameTextView.visibility = View.VISIBLE
        binding.emailTextView.visibility = View.VISIBLE
        binding.enrollmentTextView.visibility = View.VISIBLE
        binding.departmentTextView.visibility = View.VISIBLE
        binding.contactTextView.visibility = View.VISIBLE
        binding.addressTextView.visibility = View.VISIBLE

        binding.nameEditText.visibility = View.GONE
        binding.emailEditText.visibility = View.GONE
        binding.enrollmentEditText.visibility = View.GONE
        binding.departmentEditText.visibility = View.GONE
        binding.contactEditText.visibility = View.GONE
        binding.addressEditText.visibility = View.GONE

        binding.editProfileButton.text = "Edit Profile"
        isEditMode = false
    }

    private fun saveChanges() {
        // Save data from EditTexts to TextViews
        binding.nameTextView.text = binding.nameEditText.text.toString()
        binding.emailTextView.text = binding.emailEditText.text.toString()
        binding.enrollmentTextView.text = binding.enrollmentEditText.text.toString()
        binding.departmentTextView.text = binding.departmentEditText.text.toString()
        binding.contactTextView.text = binding.contactEditText.text.toString()
        binding.addressTextView.text = binding.addressEditText.text.toString()

        // Here you might also want to save the data to a database or shared preferences
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}
