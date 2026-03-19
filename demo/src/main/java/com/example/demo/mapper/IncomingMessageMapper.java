package com.example.demo.mapper;

import com.example.demo.dto.PostIncomingMessageDTO;
import com.example.demo.Entity.IncomingMessage;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IncomingMessageMapper {

    PostIncomingMessageDTO toPostIncomingMessageDTO (IncomingMessage incomingMessage);

    IncomingMessage toIncomingMessage (PostIncomingMessageDTO postIncomingMessageDTO);
}
