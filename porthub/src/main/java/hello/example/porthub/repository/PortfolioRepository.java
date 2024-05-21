package hello.example.porthub.repository;


import hello.example.porthub.domain.*;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class PortfolioRepository {

//    private static final Map<Long, PortfolioDto> portfolioItem = new ConcurrentHashMap<>();
//    //동시성 문제 해결하기 위함 ConcurrntHashMap, AtomicLong
//    private static final AtomicLong sequence = new AtomicLong(0);



    private final SqlSessionTemplate sql;

    public List<CategoryDto> findByCategory() {
        return sql.selectList("Portfolio.findByCategory");
    }


    public void PortUpload(PortfolioDto portfolioDto) {
        sql.insert("Portfolio.insertPortfolio", portfolioDto);
    }

    public void ContentUpload(ImagesDto imagesDto) {
        sql.insert("Portfolio.insertPortImages", imagesDto);
    }

    public int PortID() {
        return sql.selectOne("Portfolio.selectPortfolioID");
    }

    public List<MainPortViewDto> findAllPorts() {
        return sql.selectList("Portfolio.findAllPorts");
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
        sql.update("Portfolio.updateViewsCount", portfolioID);
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

    public List<MainPortViewDto> findAllPortsOrderByOldest() {
        return sql.selectList("Portfolio.findAllPortsOrderByOldest");
    }

    public List<MainPortViewDto> findAllPortsOrderByPopularity() {
        return sql.selectList("Portfolio.findAllPortsOrderByPopularity");
    }

    public List<MainPortViewDto> findAllPortsOrderByViews() {
        return sql.selectList("Portfolio.findAllPortsOrderByViews");
    }

    public void portfolioIncreLikes(int portfolioID) {
        sql.update("Portfolio.portfolioIncreLikes", portfolioID);
    }
    public void portfolioDecreLikes(int portfolioID) {
        sql.update("Portfolio.portfolioDecreLikes", portfolioID);
    }

    public int checkCategoryNum(int checkNum) {
        return sql.selectOne("Portfolio.checkCategoryNum", checkNum);
    }

    public List<MainPortViewDto> findAllSearchPorts(String searchQuery) {
        return sql.selectList("Portfolio.findAllSearchPorts", searchQuery);
    }

    public List<MainPortViewDto> findAllSearchPortsOrderByPopularity(String searchQuery) {
        return sql.selectList("Portfolio.findAllSearchPortsOrderByPopularity", searchQuery);
    }

    public List<MainPortViewDto> findAllSearchPortsOrderByViews(String searchQuery) {
        return sql.selectList("Portfolio.findAllSearchPortsOrderByViews", searchQuery);
    }

    public List<MainPortViewDto> findAllSearchPortsOrderByOldest(String searchQuery) {
        return sql.selectList("Portfolio.findAllSearchPortsOrderByOldest", searchQuery);
    }
}
