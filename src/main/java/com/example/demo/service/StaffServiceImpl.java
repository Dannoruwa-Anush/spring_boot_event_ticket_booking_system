package com.example.demo.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.config.enums.RoleTypeEnum;
import com.example.demo.dto.requestDTO.StaffRequestDTO;
import com.example.demo.dto.responseDTO.StaffResponseDTO;
import com.example.demo.dto.responseDTO.common.PageResponseDTO;
import com.example.demo.entity.Position;
import com.example.demo.entity.Role;
import com.example.demo.entity.Staff;
import com.example.demo.entity.User;
import com.example.demo.mapper.StaffMapper;
import com.example.demo.repository.PositionRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.StaffRepository;
import com.example.demo.repository.UserRepository;

@Service
public class StaffServiceImpl implements StaffService{

    private final StaffRepository repository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PositionRepository positionRepository;
    private final StaffMapper mapper;
    private final PasswordEncoder passwordEncoder;

    // Logger for auditing purposes
    private static final Logger logger = LoggerFactory.getLogger(StaffServiceImpl.class);

    public StaffServiceImpl(StaffRepository repository, UserRepository userRepository, RoleRepository roleRepository, PositionRepository positionRepository, StaffMapper mapper, PasswordEncoder passwordEncoder){
        this.repository = repository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.positionRepository = positionRepository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public StaffResponseDTO createStaff(StaffRequestDTO staffRequestDTO) {

        Role role = roleRepository.findByName(RoleTypeEnum.STAFF)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        Position position = positionRepository.findById(staffRequestDTO.getPositionId())
                .orElseThrow(() -> new RuntimeException("Postion not found"));        

        // Create a user
        User user = new User();
        user.setName(staffRequestDTO.getUser().getName());
        user.setEmail(staffRequestDTO.getUser().getEmail());
        user.setPassword(passwordEncoder.encode(staffRequestDTO.getUser().getPassword()));
        user.setRole(role);
        user = userRepository.save(user);

        // Create a staff
        Staff staff = mapper.toEntity(staffRequestDTO);
        staff.setUser(user);
        staff.setPosition(position);
        staff = repository.save(staff);

        return mapper.toResponseDTO(staff);
    }

    @Override
    public PageResponseDTO<StaffResponseDTO> getAllStaffMembers(Pageable pageable) {
        Page<Staff> staffMembers = repository.findAll(pageable);
        List<StaffResponseDTO> content = mapper.toResponseDTOList(staffMembers.getContent());
        
        return new PageResponseDTO<>(
                content,
                staffMembers.getNumber(),
                staffMembers.getSize(),
                staffMembers.getTotalElements(),
                staffMembers.getTotalPages(),
                staffMembers.isFirst(),
                staffMembers.isLast());
    }

    @Override
    public StaffResponseDTO getStaffById(Long id) {
      Staff staffMember = repository.findById(id)
        .orElseThrow(() -> new RuntimeException("Staff not found"));

      return mapper.toResponseDTO(staffMember);
    }

    @Override
    public StaffResponseDTO updateStaff(Long id, StaffRequestDTO staffRequestDTO) {
        Staff staff = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Staff not found"));

        mapper.updateStaffFromDTO(staffRequestDTO, staff);

        Staff updated = repository.save(staff);

        logger.info("Staff updated successfully. ID: {}", updated.getId());

        return mapper.toResponseDTO(updated);
    }

    @Override
    public void deleteStaff(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Staff is not found with id: " + id);
        }

        repository.deleteById(id);

        logger.info("Staff deleted successfully. ID: {}", id);
    } 
}
