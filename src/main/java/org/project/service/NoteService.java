package org.project.service;

import lombok.RequiredArgsConstructor;
import org.project.exception.NoteNotFoundException;
import org.project.model.Note;
import org.project.repository.NoteRepository;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;

    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }

    public Note getNoteById(Long id) {
        return noteRepository.findById(id).orElseThrow(() -> new NoteNotFoundException(
                MessageFormat.format("Note with id {0} not found", id)
        ));
    }

    public void createNote(Note note) {
        noteRepository.save(note);
    }

    public Note updateNote(Long id, Note note) {
        Optional<Note> noteOptional = noteRepository.findById(id);
        if (noteOptional.isPresent()) {
            Note noteToUpdate = noteOptional.get();
            noteToUpdate.setTitle(note.getTitle());
            noteToUpdate.setContent(note.getContent());
            noteRepository.save(noteToUpdate);

            return noteToUpdate;
        }
        throw new NoteNotFoundException(MessageFormat.format("Note with id {0} not found", id));
    }

    public void deleteNote(Long id) {
        Optional<Note> noteOptional = noteRepository.findById(id);
        if (noteOptional.isPresent()) {
            noteRepository.deleteById(id);
        } else {
            throw new NoteNotFoundException(MessageFormat.format("Note with id {0} not found", id));
        }
    }
}