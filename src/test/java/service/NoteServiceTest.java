package service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.project.exception.NoteNotFoundException;
import org.project.model.Note;
import org.project.repository.NoteRepository;
import org.project.service.NoteService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yaml")
@DisplayName("Tests for NoteService")
public class NoteServiceTest {

    @Mock
    private NoteRepository noteRepository;

    @InjectMocks
    private NoteService noteService;

    @Test
    void testGetAllNotes() {
        Note note1 = new Note();
        note1.setId(1L);
        note1.setTitle("Note 1");
        Note note2 = new Note();
        note2.setId(2L);
        note2.setTitle("Note 2");

        when(noteRepository.findAll()).thenReturn(Arrays.asList(note1, note2));

        List<Note> notes = noteService.getAllNotes();
        assertEquals(2, notes.size());
    }

    @Test
    void testGetNoteById() {
        Note note = new Note();
        note.setId(1L);
        when(noteRepository.findById(1L)).thenReturn(Optional.of(note));

        Note foundNote = noteService.getNoteById(1L);
        assertNotNull(foundNote);
        assertEquals(1L, foundNote.getId());
    }

    @Test
    void testCreateNote() {
        Note note = new Note();
        note.setTitle("New Note");
        note.setContent("This is a test note");

        noteService.createNote(note);

        verify(noteRepository, times(1)).save(note);
    }

    @Test
    void testUpdateNote() {
        Note existingNote = new Note();
        existingNote.setId(1L);
        existingNote.setTitle("Old Title");
        existingNote.setContent("Old Content");

        Note updatedNote = new Note();
        updatedNote.setTitle("Updated Title");
        updatedNote.setContent("Updated Content");

        when(noteRepository.findById(1L)).thenReturn(Optional.of(existingNote));
        when(noteRepository.save(any(Note.class))).thenReturn(updatedNote);

        Note result = noteService.updateNote(1L, updatedNote);

        assertNotNull(result);
        assertEquals("Updated Title", result.getTitle());
        assertEquals("Updated Content", result.getContent());
        verify(noteRepository, times(1)).save(existingNote);
    }

    @Test
    void testDeleteNote() {
        Note existingNote = new Note();
        existingNote.setId(1L);

        when(noteRepository.findById(1L)).thenReturn(Optional.of(existingNote));
        doNothing().when(noteRepository).deleteById(1L);

        assertDoesNotThrow(() -> noteService.deleteNote(1L));
        verify(noteRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteNote_NotFound() {
        when(noteRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoteNotFoundException.class, () -> noteService.deleteNote(1L));
    }
}
