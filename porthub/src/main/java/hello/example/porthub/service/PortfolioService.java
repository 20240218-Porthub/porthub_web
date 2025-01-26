package hello.example.porthub.service;

import hello.example.porthub.domain.*;
import hello.example.porthub.repository.MemberRepository;
import hello.example.porthub.repository.PortfolioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PortfolioService {
    private final PortfolioRepository portfolioRepository;
    private final MemberRepository memberRepository;
    private final S3Service s3Service;
    private final MemberService memberService;
    private final ImagesDto imagesDto;

    @Transactional
    public int upload(PortfolioDto portfolioDto) {
        try {
            String Category = portfolioDto.getCategoryString();
            String thumbnailUrl;
            if (portfolioDto.getThumbnail_cast() == null) {
                thumbnailUrl = "/images/None_Thumbnail.jpeg";
            } else {
                thumbnailUrl = s3Service.uploadFiles(portfolioDto.getThumbnail_cast());
            }
            portfolioDto.setThumbnail_url(thumbnailUrl);
            // UserID 가져오기
            String Email = memberService.getCurrentUserId();
            MemberDto member = memberRepository.findByUserIDtoEmail(Email);
            if (member == null) {
                throw new RuntimeException("사용자를 찾을 수 없습니다.");
            }
            portfolioDto.setAuthorID(member.getUserID());

            // 포트폴리오 카테고리와 해당하는 값 매핑
            Map<String, Integer> categoryMap = new HashMap<>();
            categoryMap.put("Development", 2);
            categoryMap.put("Music", 3);
            categoryMap.put("Design", 4);
            categoryMap.put("Editing", 5);
            categoryMap.put("Film", 6);
            categoryMap.put("Marketing", 7);
            categoryMap.put("Other", 8);

            int CategoryID = categoryMap.getOrDefault(Category, -1).intValue();
            if (CategoryID == -1) {
                throw new RuntimeException("유효하지 않은 카테고리입니다.");
            }
            portfolioDto.setCategoryID(CategoryID);

            // portfolioID를 가져옴 Images Table에 넣어주기 위함
            portfolioRepository.PortUpload(portfolioDto);
            int PortfolioID = portfolioRepository.PortID();

            imagesDto.setPortfolioID(PortfolioID);

            List<String> multipleFiles = new ArrayList<>();
            List<String> contentList = portfolioDto.getContent();

            if (portfolioDto.getFile() != null) {
                for (MultipartFile file : portfolioDto.getFile()) {
                    String multipleFile = s3Service.uploadFiles(file);
                    multipleFiles.add(multipleFile);
                }
            }

            portfolioDto.setMultipleFiles(multipleFiles); // List<String>

            int UploadSize = multipleFiles.size();

            if (contentList != null && multipleFiles != null) {

                if (portfolioDto.getFile().size() > 1) {
                    for (int i = 0; i < UploadSize; i++) {
                        imagesDto.setImage_url(multipleFiles.get(i));
                        imagesDto.setContents(contentList.get(i));
                        portfolioRepository.ContentUpload(imagesDto);
                    }
                } else {
                    String contentListString = contentList.toString();
                    String contentListWithoutBrackets = contentListString.substring(1, contentListString.length() - 1);
                    imagesDto.setImage_url(multipleFiles.get(0));
                    imagesDto.setContents(contentListWithoutBrackets);
                    portfolioRepository.ContentUpload(imagesDto);
                }


            }

            return 1; // 성공 시 1 반환
        } catch (IOException e) {
            e.printStackTrace();
            return 0; // 실패 시 0 반환
        }
    }

    public List<CategoryDto> findByCategory() {
        return portfolioRepository.findByCategory();
    }

    public int getCategoryID(String CategoryName){ return portfolioRepository.getCategoryID(CategoryName);}

    public List<MainPortViewDto> findAllPorts(int page, int pageSize, String order) {
        int offset = (page - 1) * pageSize;
        return portfolioRepository.findAllPorts(order, pageSize, offset);
    }

    public PortViewDto findportview(int portfolioID) {
        return portfolioRepository.findportview(portfolioID);
    }

    public List<ImagesDto> findportFiles(int portfolioID) {
        return portfolioRepository.findFileviews(portfolioID);
    }

    public List<PortViewDto> finduserport(int portfolioID) {
        return portfolioRepository.finduserport(portfolioID);
    }

    public void postReportdata(CopyrightReportDto copyrightReportDto) {
        portfolioRepository.insertCopyRightReportDto(copyrightReportDto);
    }

    public boolean checkHeart(int portfolioID, String authorID) {
        return portfolioRepository.checkHeart(portfolioID, authorID);
    }

    public void convertLikes(PortLikeDto portLikeDto) {
        if (portfolioRepository.ifnullHeartdata(portLikeDto)) {
            portfolioRepository.updateLikedate(portLikeDto);
        } else {
            portLikeDto.setHeart_Check(true);
            portfolioRepository.createLikedata(portLikeDto);
        }
    }

    public boolean checkFollow(int authorID, String currentEmail) {
        int CurrentID = portfolioRepository.findByUserIDtoEmailcheck(currentEmail);
        return portfolioRepository.checkFollow(authorID, CurrentID);
    }

    public void following(int authorID, String currentUseremail) {
        int CurrentID = portfolioRepository.findByUserIDtoEmailcheck(currentUseremail);
        portfolioRepository.following(authorID, CurrentID);
    }

    public void unfollow(int authorID, String currentUseremail) {
        int CurrentID = portfolioRepository.findByUserIDtoEmailcheck(currentUseremail);
        portfolioRepository.unfollow(authorID, CurrentID);
    }

    public void updateViewsCount(int portfolioID) {
        portfolioRepository.updateViewsCount(portfolioID);
    }

    public void portdelete(int portfolioID) {
        portfolioRepository.deletePortfolioData(portfolioID);
        portfolioRepository.deletePortfolio(portfolioID);
    }

    public int UpdatePortfolio(PortfolioDto portfolioDto) {
        try {
            String thumbnailUrl;
            if (portfolioDto.getThumbnail_cast() == null) {
//                thumbnailUrl = "https://porthub2.s3.ap-northeast-2.amazonaws.com/None_Thumbnail.jpeg";
                thumbnailUrl = null;
            } else {
                thumbnailUrl = s3Service.uploadFiles(portfolioDto.getThumbnail_cast());
            }
            portfolioDto.setThumbnail_url(thumbnailUrl);

            // portfolioID를 가져옴 Images Table에 넣어주기 위함
            portfolioRepository.PortUpdate(portfolioDto);

            int PortfolioID = portfolioDto.getPortfolioID();

            imagesDto.setPortfolioID(PortfolioID);

            List<String> multipleFiles = new ArrayList<>();
            List<String> contentList = portfolioDto.getContent();

            if (portfolioDto.getFile() != null) {
                for (MultipartFile file : portfolioDto.getFile()) {
                    String multipleFile = s3Service.uploadFiles(file);
                    multipleFiles.add(multipleFile);
                }
            }


            portfolioDto.setMultipleFiles(multipleFiles); // List<String>

            int UploadSize = multipleFiles.size();

            List<Integer> ImageFileID = portfolioRepository.getImagesID(PortfolioID);

            if (contentList != null && multipleFiles != null) {
                if (portfolioDto.getFile().size() > 1) {
                    for (int i = 0; i < UploadSize; i++) {
                        imagesDto.setImage_url(multipleFiles.get(i));
                        imagesDto.setContents(contentList.get(i));
                        imagesDto.setImagesFileID(ImageFileID.get(i));
                        portfolioRepository.ContentUpdate(imagesDto);
                    }
                } else {
                    String contentListString = contentList.toString();
                    String contentListWithoutBrackets = contentListString.substring(1, contentListString.length() - 1);
                    imagesDto.setImage_url(multipleFiles.get(0));
                    imagesDto.setContents(contentListWithoutBrackets);
                    imagesDto.setImagesFileID(ImageFileID.get(0));
                    portfolioRepository.ContentUpdate(imagesDto);
                }


            }

            return 1; // 성공 시 1 반환
        } catch (IOException e) {
            e.printStackTrace();
            return 0; // 실패 시 0 반환
        }
    }

    public void portfolioIncreLikes(int portfolioID) {
        portfolioRepository.portfolioIncreLikes(portfolioID);
    }

    public void portfolioDecreLikes(int portfolioID) {
        portfolioRepository.portfolioDecreLikes(portfolioID);
    }

    public int checkCategoryNum(int checkNum) {
        if (checkNum > 0) {
            return portfolioRepository.checkCategoryNum(checkNum);
        } else {
            return 0;
        }
    }

    public List<MainPortViewDto> findAllSearchPorts(int page, int pageSize, String order, String searchQuery, int checkNum) {
        int offset = (page - 1) * pageSize;
        return portfolioRepository.findAllSearchPorts(order, pageSize, offset, searchQuery, checkNum);
    }



    public List<PopularDto> findByPopular() {

        return portfolioRepository.findByPopular();
    }

    public void updatePopularTask() {
        // 모든 포트폴리오를 가져옴
        List<CalculatePopularDto> portfolios = portfolioRepository.findAllCalcultePorts();
        if (portfolios == null || portfolios.isEmpty()) {
            return; // 데이터가 없으면 작업 중단
        }

        // 순위 계산
        Map<Integer, Integer> portfolioRankMap = calculatePortfolioRank(portfolios);

        for (Map.Entry<Integer, Integer> entry : portfolioRankMap.entrySet()) {
            int getUserID = entry.getKey();
            int rank = entry.getValue();

            PopularDto resetPopulars = portfolioRepository.findUserByAuthor(getUserID);
            resetPopulars.setPopularID(rank);

            portfolioRepository.saveOrUpdateRank(resetPopulars);
        }
    }


    private Map<Integer, Integer> calculatePortfolioRank(List<CalculatePopularDto> portfolios) {
        // Hearts_count와 Views_count의 총합 계산
        long totalHeartsCount = portfolios.stream()
                .mapToLong(CalculatePopularDto::getHearts_count)
                .sum();
        long totalViewsCount = portfolios.stream()
                .mapToLong(CalculatePopularDto::getViews_count)
                .sum();

        // 총합이 0인 경우 1로 설정
        long safeHeartsCount;
        if (totalHeartsCount == 0) {
            safeHeartsCount = 1;
        } else {
            safeHeartsCount = totalHeartsCount;
        }

        long safeViewsCount;
        if (totalViewsCount == 0) {
            safeViewsCount = 1;
        } else {
            safeViewsCount = totalViewsCount;
        }

        // 포트폴리오 점수 계산 (가중치: Hearts 80%, Views 20%)
        Map<Integer, Double> portfolioScoreMap = portfolios.stream()
                .collect(Collectors.toMap(
                        CalculatePopularDto::getAuthorID,
                        portfolio -> 0.8 * portfolio.getHearts_count() / (double) safeHeartsCount +
                                0.2 * portfolio.getViews_count() / (double) safeViewsCount,
                        Double::sum
                ));

        // 점수를 기준으로 순위 매핑
        List<Map.Entry<Integer, Double>> sortedPortfolios = new ArrayList<>(portfolioScoreMap.entrySet());
        sortedPortfolios.sort(Map.Entry.<Integer, Double>comparingByValue().reversed());

        // 순위 매핑
        Map<Integer, Integer> portfolioRankMap = new LinkedHashMap<>();
        int rank = 1;
        for (Map.Entry<Integer, Double> entry : sortedPortfolios) {
            portfolioRankMap.put(entry.getKey(), rank++);
        }
        return portfolioRankMap.entrySet().stream()
                .limit(3)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public List<Integer> findLikesByEmail(String userEmail) {
        return portfolioRepository.findLikesByEmail(userEmail);
    }

    public List<MainPortViewDto> findSelectListPorts(List<Integer> IDs) {
        return portfolioRepository.findSelectListPorts(IDs);
    }

    public int findUserIDbyUserName(String userName) {
        return portfolioRepository.findUserIDbyUserName(userName);
    }

    public List<PopularDto> getFollowList(List<Integer> userid) {
        return portfolioRepository.findgetFollowListbyUserID(userid);
    }

    public int getPortfolioSize() {
        return portfolioRepository.getPortfolioSize();
    }

    public List<MainPortViewDto> findCategoryPorts(int page, int pageSize, String order, int checkNum) {

        int offset = (page - 1) * pageSize;
        return portfolioRepository.findCategoryPorts(order, pageSize, offset, checkNum);
    }

}

