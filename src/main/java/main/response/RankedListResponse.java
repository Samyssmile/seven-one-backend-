package main.response;

import main.dto.AuthenticatedUserDto;

import java.util.List;

public class RankedListResponse {

    private List<AuthenticatedUserDto> rankedList;


    public RankedListResponse() {
    }

    public List<AuthenticatedUserDto> getRankedList() {
        return rankedList;
    }

    public void setRankedList(List<AuthenticatedUserDto> rankedList) {
        this.rankedList = rankedList;
    }
}
