package hello.example.porthub.repository;


import hello.example.porthub.domain.*;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@RequiredArgsConstructor
public class PortfolioRepository {

//    //동시성 문제 해결하기 위함 ConcurrntHashMap 현재 서비스는 단일 스레드 환경이긴 함

    private final SqlSessionTemplate sql;

    public List<CategoryDto> findByCategory() {
        return sql.selectList("Portfolio.findByCategory");
    }

    public int getCategoryID(String Category){return sql.selectOne("Portfolio.getCategoryID", Category);}

    public void PortUpload(PortfolioDto portfolioDto) {
        sql.insert("Portfolio.insertPortfolio", portfolioDto);
    }

    public void ContentUpload(ImagesDto imagesDto) {
        sql.insert("Portfolio.insertPortImages", imagesDto);
    }

    public int PortID() {
        return sql.selectOne("Portfolio.selectPortfolioID");
    }

    public PortViewDto findportview(int portfolioID) {
        return sql.selectOne("Portfolio.findPortfolioByPortfolioID", portfolioID);
    }

    public List<ImagesDto> findFileviews(int portfolioID) {
        return sql.selectList("Portfolio.findPortFilesByPortfolioID", portfolioID);
    }

    public List<PortViewDto> finduserport(int portfolioID) {
        return sql.selectList("Portfolio.finduserport", portfolioID);
    }

    public void insertCopyRightReportDto(CopyrightReportDto copyrightReportDto) {
        sql.insert("Portfolio.insertCopyrightDto", copyrightReportDto);
    }

    public boolean checkHeart(int portfolioID, String authorID) {
        Map<String, Object> params = new HashMap<>();
        params.put("portfolioID", portfolioID);
        params.put("authorID", authorID);
        return sql.selectOne("Portfolio.findBycheckHeart", params);
    }

    public boolean ifnullHeartdata(PortLikeDto portLikeDto) {
        return sql.selectOne("Portfolio.ifnullHeartdata", portLikeDto);
    }

    public void createLikedata(PortLikeDto portLikeDto) {
        sql.insert("Portfolio.insertPortLikes", portLikeDto);
    }

    public void updateLikedate(PortLikeDto portLikeDto) {
        sql.update("Portfolio.updatePortLikes", portLikeDto);
    }
    public boolean checkFollow(int authorID, int currentID) {
        Map<String, Object> checkFollow = new HashMap<>();
        checkFollow.put("authorID", authorID);
        checkFollow.put("currentID", currentID);
        return sql.selectOne("Portfolio.findByCheckFollow", checkFollow);
    }

    public int findByUserIDtoEmailcheck(String currentEmail) {
        return sql.selectOne("Member.findByUserIDtoEmailcheck", currentEmail);
    }

    public void following(int authorID, int currentID) {
        Map<String, Object> FollowData = new HashMap<>();
        FollowData.put("authorID", authorID);
        FollowData.put("currentID", currentID);
        sql.insert("Portfolio.followInsert", FollowData);
    }

    public void unfollow(int authorID, int currentID) {
        Map<String, Object> FollowData = new HashMap<>();
        FollowData.put("authorID", authorID);
        FollowData.put("currentID", currentID);
        sql.delete("Portfolio.unfollowDelete", FollowData);
    }

    public void updateViewsCount(int portfolioID) {


        try {
            Thread.sleep(10); // 동시성 문제를 유발하기 위한 대기 시간
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        sql.update("Portfolio.updateViewsCount", portfolioID);
    }

    public int getViewsCount(int portfolioID) {
        return sql.selectOne("Portfolio.selectViewsCount", portfolioID);
    }

    public void deletePortfolio(int portfolioID) {
        sql.delete("Portfolio.deletePortfolio", portfolioID);
    }

    public void deletePortfolioData(int portfolioID) {
        sql.delete("Portfolio.deletePortfolioData", portfolioID);
    }

    public void PortUpdate(PortfolioDto portfolioDto) {
        if (portfolioDto.getThumbnail_url() == null) {

            sql.update("Portfolio.PortUpdateNoneThumb", portfolioDto);
        } else {
            sql.update("Portfolio.PortUpdate", portfolioDto);
        }
    }

    public List<Integer> getImagesID(int portfolioID) {
        return sql.selectList("Portfolio.getImagesID", portfolioID);
    }

    public void ContentUpdate(ImagesDto imagesDto) {
        if (imagesDto.getImage_url() == null) {
            sql.update("Portfolio.ContentUpdateNull", imagesDto);
        } else {
            sql.update("Portfolio.ContentUpdate", imagesDto);
        }
    }

    public void portfolioIncreLikes(int portfolioID) {

        try {
            Thread.sleep(10); // 동시성 문제를 유발하기 위한 대기 시간
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        sql.update("Portfolio.portfolioIncreLikes", portfolioID);
    }

    public void portfolioDecreLikes(int portfolioID) {

        sql.update("Portfolio.portfolioDecreLikes", portfolioID);
    }
    public int checkCategoryNum(int checkNum) {
        return sql.selectOne("Portfolio.checkCategoryNum", checkNum);
    }

    public List<MainPortViewDto> findAllSearchPorts(String order, int pageSize, int offset, String searchQuery, int checkNum) {
        Map<String, Object> params = new HashMap<>();
        params.put("order", order);
        params.put("pageSize", pageSize);
        params.put("offset", offset);
        params.put("checkNum", checkNum);
        params.put("searchQuery", searchQuery);

        return sql.selectList("Portfolio.findAllSearchPorts", params);
    }

    public List<PopularDto> findByPopular()  {
        return sql.selectList("Portfolio.findByPopular");
    }

    public List<CalculatePopularDto> findAllCalcultePorts() {
        return sql.selectList("Portfolio.findAllCalcultePorts");
    }

    public PopularDto findUserByAuthor(int getUserID) {
        return sql.selectOne("Portfolio.findUserByAuthor", getUserID);
    }

    public List<Integer> findLikesByEmail(String userEmail) {
        return sql.selectList("Portfolio.findLikesByEmail",userEmail);
    }

    public List<MainPortViewDto> findSelectListPorts(List<Integer> iDs) {
        return sql.selectList("Portfolio.findSelectListPorts", iDs);
    }

    public int findUserIDbyUserName(String userName) {
        return sql.selectOne("Portfolio.findUserIDbyUserName", userName);
    }

    public List<PopularDto> findgetFollowListbyUserID(List<Integer> userid) {
        return sql.selectList("Portfolio.findgetFollowListbyUserID", userid);
    }

    public void saveOrUpdateRank(PopularDto popularDto) {
        Integer existingId = sql.selectOne("Portfolio.checkExistsByPopularID", popularDto.getPopularID());
        if (existingId != null) {
            // 데이터가 존재하면 UPDATE 실행
            sql.update("Portfolio.updateByRank", popularDto);
        } else {
            // 데이터가 없으면 INSERT 실행
            sql.insert("Portfolio.insertByRank", popularDto);
        }
    }

    public List<MainPortViewDto> findAllPorts(String order, int pageSize, int offset) {
        Map<String, Object> params = new HashMap<>();
        params.put("order", order);
        params.put("pageSize", pageSize);
        params.put("offset", offset);

        return sql.selectList("Portfolio.findAllPorts", params);
    }

    public int getPortfolioSize() {
        return sql.selectOne("Portfolio.getPortfolioSize");
    }

    public List<MainPortViewDto> findCategoryPorts(String order, int pageSize, int offset, int checkNum) {
        Map<String, Object> params = new HashMap<>();
        params.put("order", order);
        params.put("pageSize", pageSize);
        params.put("offset", offset);
        params.put("checkNum", checkNum);
        return sql.selectList("Portfolio.findCategoryPorts", params);
    }
}
