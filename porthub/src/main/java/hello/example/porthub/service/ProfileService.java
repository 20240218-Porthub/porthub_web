package hello.example.porthub.service;


import hello.example.porthub.domain.MainPortViewDto;
import hello.example.porthub.domain.MemberDto;
import hello.example.porthub.domain.ProfileDto;
import hello.example.porthub.repository.MemberRepository;
import hello.example.porthub.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;
    public List<MainPortViewDto> findPortByUserID(int UserID){ return profileRepository.findportByUserID(UserID);}

//    public int cntFollower(int id){return profileRepository.cntFollower(id);}
//
//    public int cntFollowing(int id){return profileRepository.cntFollowing(id);}

}
