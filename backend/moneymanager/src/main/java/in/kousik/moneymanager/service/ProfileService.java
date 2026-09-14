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
    private  final EmailService emailService;

    public ProfileDTO registerProfile(ProfileDTO profileDTO){
        //1. convert profileDTO to profileEntity
        ProfileEntity newProfile = convertToEntity(profileDTO);
        newProfile.setActivationToken(UUID.randomUUID().toString());
        
        //2.save it using profileRepository
       newProfile= profileRepository.save(newProfile);
       //* send activation email
        String activationLink="http://localhost:8080/api/v1.0/activate?token="+newProfile.getActivationToken();
        String subject="Activate your Money Manager account";
        String body="Click on the following link to activate your account: "+activationLink;
        emailService.sendEmail(newProfile.getEmail(),subject,body);
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
    //validate token
    public boolean activateProfile(String activationToken){
        return profileRepository.findByActivationToken(activationToken)
                .map(profile->{
                    profile.setIsActive(true);
                    profileRepository.save(profile);
                    return true;
                })
                .orElse(false);
    }
}
