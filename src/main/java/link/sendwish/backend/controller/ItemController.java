package link.sendwish.backend.controller;

import link.sendwish.backend.common.exception.DtoNullException;
import link.sendwish.backend.dtos.*;
import link.sendwish.backend.dtos.item.*;
import link.sendwish.backend.entity.Collection;
import link.sendwish.backend.entity.Item;
import link.sendwish.backend.entity.Member;
import link.sendwish.backend.service.CollectionService;
import link.sendwish.backend.service.ItemService;
import link.sendwish.backend.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Semaphore;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ItemController {

    private final ItemService itemService;
    private final MemberService memberService;
    private final CollectionService collectionService;
    private final LinkedList queue;

    // scrapping-server 연결
    public JSONObject createHttpRequestAndSend(String url) throws InterruptedException {
        RestTemplate restTemplate = new RestTemplate();

        // Request_body 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("url", url);

        // Request_header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Request_header, Request_body 합친 entity
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);


        JSONObject jsonObject = new JSONObject(restTemplate
                    .postForObject("http://127.0.0.1:5000/webscrap", entity, String.class));

        return jsonObject;
    }


    //등록된 item id 값 리턴
    @PostMapping("/item/parsing")
    public ResponseEntity<?> createItem(@RequestBody ItemCreateRequestDto dto) {
        try {
            if(dto.getUrl() == null){
                throw new DtoNullException();
            }
            Item find = itemService.findItem(dto.getUrl());
            if(find != null){
                itemService.checkMemberReferenceByItem(find, dto.getNickname());
                return ResponseEntity.ok().body(find.getId());
            }

            /*
            * Python Server 호출, DB에 Item 등록
            * */
            queue.offer(dto.getUrl());
            String url = (String) queue.poll();

            JSONObject jsonObject = createHttpRequestAndSend(url);

            Item item = Item.builder()
                    .name((String)jsonObject.get("title"))
                    .price((Integer)jsonObject.get("price"))
                    .imgUrl((String)jsonObject.get("img"))
                    .originUrl((String)jsonObject.get("url"))
                    .memberItems(new ArrayList<>())
                    .collectionItems(new ArrayList<>())
                    .build();
            Long saveItem = itemService.saveItem(item, dto.getNickname());

            return ResponseEntity.ok().body(saveItem);
        }catch (Exception e) {
            e.printStackTrace();
            ResponseErrorDto errorDto = ResponseErrorDto.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.internalServerError().body(errorDto);
        }
    }

    @PostMapping("/item/enrollment")
    public ResponseEntity<?> enrollItem(@RequestBody ItemEnrollmentRequestDto dto) {
        try {
            if (dto.getCollectionId() == null || dto.getItemIdList() == null || dto.getNickname() == null){
                throw new DtoNullException();
            }

            /*
            * find Collection 후 , Item 찾아서 (JPA 1차 캐시) 해당 Item을 Collection에 저장
            * 하고 나서 해당 item 상세 정보를 return
            * */
            ItemListResponseDto itemListResponseDto = itemService.enrollItemToCollection(dto.getNickname(), dto.getCollectionId(), dto.getItemIdList());
            return ResponseEntity.ok().body(itemListResponseDto);
        }catch (Exception e) {
            e.printStackTrace();
            ResponseErrorDto errorDto = ResponseErrorDto.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.internalServerError().body(errorDto);
        }
    }

    @DeleteMapping("/items")
    public ResponseEntity<?> deleteItem(@RequestBody ItemDeleteRequestDto dto) {
        try{
            if (dto.getNickname() == null || dto.getItemIdList() == null){
                throw new DtoNullException();
            }
            String nickname = dto.getNickname();
            List<Long> itemIdList = dto.getItemIdList();

            /*
             * find Collection 후 , Item 찾아서 (JPA 1차 캐시) 해당 Item을 Collection에 저장
             * 하고 나서 해당 item 상세 정보를 return
             * */
            ItemDeleteResponseDto dtos = itemService.deleteItem(nickname, itemIdList);

            return ResponseEntity.ok().body(dtos);
        }catch (Exception e) {
            e.printStackTrace();
            ResponseErrorDto errorDto = ResponseErrorDto.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.internalServerError().body(errorDto);
        }
    }

    @GetMapping("/items/{nickname}")
    public ResponseEntity<?> getItemsByMember(@PathVariable("nickname") String nickname) {
        try {
            Member member = memberService.findMember(nickname);
            List<ItemResponseDto> memberItem = itemService.findItemByMember(member);
            return ResponseEntity.ok().body(memberItem);
        }catch (Exception e) {
            e.printStackTrace();
            ResponseErrorDto errorDto = ResponseErrorDto.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.internalServerError().body(errorDto);
        }
    }
}
