package com.example.oauth.service;

import com.example.oauth.dto.PageDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PageService {

    public List<PageDto> getPageDtoList(int totalPage, int currentPage){
        List<PageDto> pageDtoList= new ArrayList<>();
        for(int i = 1; i <= totalPage; i++){
            PageDto pageDto = new PageDto();
            pageDto.setPage(i);
            if(i == currentPage)
                pageDto.setCheck(true);
            else
                pageDto.setCheck(false);
            pageDtoList.add(pageDto);
        }
        return pageDtoList;
    }

}
