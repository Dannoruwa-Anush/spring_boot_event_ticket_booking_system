
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.dto.requestDTO.EventRequestDTO;
import com.example.demo.dto.responseDTO.EventResponseDTO;
import com.example.demo.dto.responseDTO.common.PageResponseDTO;

@Service
public class EventService {
    EventResponseDTO createEvent(EventRequestDTO eventRequestDTO);
    PageResponseDTO<EventResponseDTO> getAllEvents(Pageable pageable);
    EventResponseDTO getEventById(Long id);
    EventResponseDTO updateEvent(Long id, EventRequestDTO eventRequestDTO);
    void deleteEvent(Long id);
}
