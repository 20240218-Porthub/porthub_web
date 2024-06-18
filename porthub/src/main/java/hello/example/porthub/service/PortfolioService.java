package hello.example.porthub.service;

import hello.example.porthub.domain.*;
import hello.example.porthub.repository.MemberRepository;
import hello.example.porthub.repository.PortfolioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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

    public int upload(PortfolioDto portfolioDto) {
        try {
            String Category = portfolioDto.getCategoryString();
            String thumbnailUrl;
            if (portfolioDto.getThumbnail_cast() == null) {
                thumbnailUrl = "https://porthub2.s3.ap-northeast-2.amazonaws.com/None_Thumbnail.jpeg";
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

    public List<MainPortViewDto> findAllPorts() {
        return portfolioRepository.findAllPorts();
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

    public List<MainPortViewDto> findAllPortsOrderByOldest() {

        return portfolioRepository.findAllPortsOrderByOldest();

    }

    public List<MainPortViewDto> findAllPortsOrderByPopularity() {
        return portfolioRepository.findAllPortsOrderByPopularity();
    }

    public List<MainPortViewDto> findAllPortsOrderByViews() {
        return portfolioRepository.findAllPortsOrderByViews();
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

    public List<MainPortViewDto> findPortsByCategory(List<MainPortViewDto> mainPortViewDtoList, int checkNum) {

        List<MainPortViewDto> selectedPortViewDtoList = new ArrayList<>(); // 선택된 포트폴리오를 저장할 리스트

        // 카테고리 넘버가 checkNum과 일치하는 포트폴리오만 selectedPortViewDtoList에 추가
        for (MainPortViewDto portViewDto : mainPortViewDtoList) {
            if (portViewDto.getCategoryID() == checkNum) {
                selectedPortViewDtoList.add(portViewDto);
            }
        }

        return selectedPortViewDtoList;
    }

    public List<MainPortViewDto> findAllSearchPorts(String searchQuery) {
        return portfolioRepository.findAllSearchPorts(searchQuery);
    }

    public List<MainPortViewDto> findAllSearchPortsOrderByPopularity(String searchQuery) {
        return portfolioRepository.findAllSearchPortsOrderByPopularity(searchQuery);
    }

    public List<MainPortViewDto> findAllSearchPortsOrderByViews(String searchQuery) {
        return portfolioRepository.findAllSearchPortsOrderByViews(searchQuery);
    }

    public List<MainPortViewDto> findAllSearchPortsOrderByOldest(String searchQuery) {
        return portfolioRepository.findAllSearchPortsOrderByOldest(searchQuery);
    }


    public List<PopularDto> findByPopular() {

        return portfolioRepository.findByPopular();
    }

    public void updatePopularTask() {
        // 모든 포트폴리오를 가져옴
        List<CalculatePopularDto> portfolios = portfolioRepository.findAllCalcultePorts();

        if (portfolios == null || portfolios.isEmpty()) {
            return;
        }
        PopularDto resetPopulars;
        // 포트폴리오의 순위를 계산하여 매핑할 맵
        Map<Integer, Integer> portfolioRankMap = calculatePortfolioRank(portfolios);
        for (Map.Entry<Integer, Integer> entry : portfolioRankMap.entrySet()) {
            int getUserID = entry.getKey();
            int rank = entry.getValue();
            resetPopulars = portfolioRepository.findUserByAuthor(getUserID);
            resetPopulars.setPopularID(rank);
            portfolioRepository.updateByRank(resetPopulars);
        }

    }


    private Map<Integer, Integer> calculatePortfolioRank(List<CalculatePopularDto> portfolios) {
        // Hearts_count와 Views_count의 총합을 계산
        long totalHeartsCount = portfolios.stream()
                .mapToLong(CalculatePopularDto::getHearts_count)
                .sum();
        long totalViewsCount = portfolios.stream()
                .mapToLong(CalculatePopularDto::getViews_count)
                .sum();

        if (totalHeartsCount == 0 || totalViewsCount == 0) {
            throw new RuntimeException("Hearts_count 또는 Views_count가 0입니다.");
        }

        // 포트폴리오를 Hearts_count의 80%와 Views_count의 20% 비율로 가중치를 부여하여 점수를 계산
        Map<Integer, Double> portfolioScoreMap = portfolios.stream()
                .collect(Collectors.toMap(
                        CalculatePopularDto::getAuthorID,
                        portfolio -> 0.8 * portfolio.getHearts_count() / (double) totalHeartsCount +
                                0.2 * portfolio.getViews_count() / (double) totalViewsCount,
                        Double::sum // 충돌 시 점수를 합산
                ));

        // 중복되는 AuthorID를 처리하고 포트폴리오의 순위를 재배열
        List<Map.Entry<Integer, Double>> sortedPortfolios = new ArrayList<>(portfolioScoreMap.entrySet());
        sortedPortfolios.sort(Map.Entry.<Integer, Double>comparingByValue().reversed());

        // 순위 매핑
        Map<Integer, Integer> portfolioRankMap = new LinkedHashMap<>();
        int rank = 1;
        for (Map.Entry<Integer, Double> entry : sortedPortfolios) {
            portfolioRankMap.put(entry.getKey(), rank++);
        }

        // 상위 3개 랭크 반환
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
}

