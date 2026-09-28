
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.requestDTO.BookingRequestDTO;
import com.example.demo.dto.responseDTO.BookingResponseDTO;
import com.example.demo.dto.responseDTO.common.PageResponseDTO;

@Service
public class BookingServive {
    BookingResponseDTO createBooking(BookingRequestDTO dto);
    PageResponseDTO<BookingResponseDTO> getAllBookings(Pageable pageable);
    BookingResponseDTO getBookingById(Long id);
    BookingResponseDTO updateBoking(Long id, BookingRequestDTO dto);
    void deleteBooking(Long id);
}
