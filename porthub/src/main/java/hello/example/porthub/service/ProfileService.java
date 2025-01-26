package hello.example.porthub.service;


import hello.example.porthub.domain.MainPortViewDto;
import hello.example.porthub.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;
    public List<MainPortViewDto> findPortByUserID(int UserID, int page, int pageSize){
        int offset = (page - 1) * pageSize;
        return profileRepository.findportByUserID(UserID, pageSize, offset);
    }

    public int countPortfoliosByUserID(int userid) {
        return profileRepository.countPortfoliosByUserID(userid);
    }

}
