package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.project.api.NoteController;
import org.project.model.Note;
import org.project.repository.NoteRepository;
import org.project.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(classes = {NoteRepository.class, NoteService.class})
@AutoConfigureMockMvc(webClientEnabled = false)
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yaml")
@DisplayName("Tests for NoteController")
public class NoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NoteRepository noteRepository;

    @Mock
    private NoteService noteService;

    @InjectMocks
    private NoteController noteController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private Note note;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(noteController)
                .build();

        note = Note.builder()
                .id(1L)
                .title("Sample Note")
                .content("Sample Content")
                .build();
    }

    @Test
    void testGetAllNotes() throws Exception {
        when(noteService.getAllNotes()).thenReturn(List.of(note));

        mockMvc.perform(get("/api/notes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("Sample Note"))
                .andExpect(jsonPath("$[0].content").value("Sample Content"));

        verify(noteService, times(1)).getAllNotes();
    }

    @Test
    void testGetNoteById() throws Exception {
        when(noteService.getNoteById(1L)).thenReturn(note);

        mockMvc.perform(get("/api/notes/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Sample Note"))
                .andExpect(jsonPath("$.content").value("Sample Content"));

        verify(noteService, times(1)).getNoteById(1L);
    }

    @Test
    void testCreateNote() throws Exception {
        doNothing().when(noteService).createNote(note);

        mockMvc.perform(post("/api/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(note)))
                .andExpect(status().isOk());

        verify(noteService, times(1)).createNote(any(Note.class));
    }

    @Test
    void testUpdateNote() throws Exception {
        when(noteService.updateNote(eq(1L), any(Note.class))).thenReturn(note);

        mockMvc.perform(put("/api/notes/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(note)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Sample Note"))
                .andExpect(jsonPath("$.content").value("Sample Content"));

        verify(noteService, times(1)).updateNote(eq(1L), any(Note.class));
    }

    @Test
    void testDeleteNote() throws Exception {
        doNothing().when(noteService).deleteNote(1L);

        mockMvc.perform(delete("/api/notes/{id}", 1L))
                .andExpect(status().isOk());

        verify(noteService, times(1)).deleteNote(1L);
    }
}
