package com.ptb.notetaker

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.ptb.notetaker.databinding.FragmentNoteListBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class NoteListFragment : Fragment() {

    private var _binding: FragmentNoteListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentNoteListBinding.inflate(inflater, container, false)

        binding.listNotes.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, DataManager.notes)
        binding.listNotes.setOnItemClickListener() { parent, view, position, id ->
            val activityIntent =Intent(requireContext(), MainActivity::class.java)
            activityIntent.putExtra(NOTE_POSITION, position )
            startActivity(activityIntent)
        }


        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onResume() {
        super.onResume()
        (binding.listNotes.adapter as ArrayAdapter<NoteInfo>).notifyDataSetChanged()

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}