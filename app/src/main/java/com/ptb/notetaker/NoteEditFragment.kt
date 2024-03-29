package com.ptb.notetaker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavArgs
import com.ptb.notetaker.databinding.FragmentNoteEditBinding
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class NoteEditFragment : Fragment(), NavArgs {
    private lateinit var noteEditViewModel: NoteEditViewModel

private var _binding: FragmentNoteEditBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

      _binding = FragmentNoteEditBinding.inflate(inflater, container, false)
      return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapterCourses = ArrayAdapter<CourseInfo>(binding.root.context,
            android.R.layout.simple_spinner_item, DataManager.courses.values.toList())
        adapterCourses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCourses.adapter = adapterCourses
        noteEditViewModel = ViewModelProvider(requireActivity())[NoteEditViewModel::class.java]
        noteEditViewModel.updateNotePosition(requireActivity().intent.getIntExtra(NOTE_POSITION, POSITION_NOT_SET))
        noteEditViewModel.notePosition.observe(viewLifecycleOwner) { notePosition ->
            notePosition?.let {
                if ( notePosition == POSITION_NOT_SET) {
                    DataManager.notes.add(NoteInfo())
                    noteEditViewModel.updateNotePosition(DataManager.notes.lastIndex)
                }
                else if (notePosition < 0) {
                    DataManager.notes.removeAt(DataManager.notes.lastIndex)
                }
                displayNote()
            }
            noteEditViewModel.saveRequest.observe(viewLifecycleOwner) { saveRequest ->
                saveRequest?.let {
                    if (saveRequest) {
                        saveNote()
                        Snackbar.make(view, "Note saved", Snackbar.LENGTH_LONG).show()
                        noteEditViewModel.triggerSaveComplete()
                    }
                }
            }
        }

        if (noteEditViewModel.notePosition.value != POSITION_NOT_SET)
            displayNote()

        binding.backButton.setOnClickListener {
            findNavController().navigate(R.id.action_NoteEditFragment_to_NoteListFragment)
        }
    }

    private fun displayNote() {
        val nextIndex =noteEditViewModel.notePosition.value!!
        if (nextIndex < 0) return
        val note = DataManager.notes[noteEditViewModel.notePosition.value!!]
        binding.textNoteTitle.setText(note.title)
        binding.textNote.setText(note.text)

        val coursePosition = DataManager.courses.values.indexOf(note.course)
        binding.spinnerCourses.setSelection(coursePosition)
    }

    private fun saveNote() {
        val note = DataManager.notes[noteEditViewModel.notePosition.value!!]
        note.title = binding.textNoteTitle.text.toString()
        note.text = binding.textNote.text.toString()
        note.course = binding.spinnerCourses.selectedItem as CourseInfo
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}