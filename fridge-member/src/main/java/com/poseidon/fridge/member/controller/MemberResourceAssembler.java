package com.poseidon.fridge.member.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import com.poseidon.fridge.member.model.Member;

@Component
public class MemberResourceAssembler implements ResourceAssembler<Member, Resource<Member>> {

    @Override
    public Resource<Member> toResource(Member member) {
        return new Resource<>(member, 
                linkTo(methodOn(MemberController.class).findByUsername(member.getUsername())).withSelfRel());
    }

}
