package in.kousik.moneymanager.service;

import in.kousik.moneymanager.dto.ProfileDTO;
import in.kousik.moneymanager.entity.ProfileEntity;
import in.kousik.moneymanager.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;

    public ProfileDTO registerProfile(ProfileDTO profileDTO){
        //1. convert profileDTO to profileEntity
        ProfileEntity newProfile = convertToEntity(profileDTO);
        newProfile.setActivationToken(UUID.randomUUID().toString());
        
        //2.save it using profileRepository
       newProfile= profileRepository.save(newProfile);
        //3.then convert it back to profileDto

      return  convertToDTO(newProfile);
        

    }

    private ProfileEntity convertToEntity(ProfileDTO profileDTO) {
        return ProfileEntity.builder()
                .id(profileDTO.getId())
                .fullName(profileDTO.getFullName())
                .email(profileDTO.getEmail())
                .password(profileDTO.getPassword())
                .profileImageUrl(profileDTO.getProfileImageUrl())
                .createdAt(profileDTO.getCreatedAt())
                .updateAt(profileDTO.getUpdateAt())
                .build();
    }
    private ProfileDTO convertToDTO(ProfileEntity profileEntity){
        return ProfileDTO.builder()
                .id(profileEntity.getId())
                .fullName(profileEntity.getFullName())
                .email(profileEntity.getEmail())
                .profileImageUrl(profileEntity.getProfileImageUrl())
                .createdAt(profileEntity.getCreatedAt())
                .updateAt(profileEntity.getUpdateAt())
                .build();
    }
}
