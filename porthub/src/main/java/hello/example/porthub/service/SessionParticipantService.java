package hello.example.porthub.service;

import hello.example.porthub.domain.SessionParticipantDto;
import hello.example.porthub.repository.SessionParticipantMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SessionParticipantService {

    private final SessionParticipantMapper sessionParticipantMapper;

    public SessionParticipantService(SessionParticipantMapper sessionParticipantMapper) {
        this.sessionParticipantMapper = sessionParticipantMapper;
    }

    public void addParticipantToSession(String sessionKey, int userId) {
        // Check if the participant already exists in the session
        if (!sessionParticipantMapper.isSessionParticipant(sessionKey, userId)) {
            sessionParticipantMapper.insertSessionParticipant(sessionKey, userId);
        }
    }

    public Integer findOtherParticipant(String sessionKey, int userId) {
        return sessionParticipantMapper.findOtherParticipant(sessionKey, userId);
    }

    public List<String> findSessionKeysByUserId(int userId) {
        return sessionParticipantMapper.findSessionKeysByUserId(userId);
    }
}

