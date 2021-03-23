package com.project.springBoot.util.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.project.springBoot.util.dto.Member;

@Mapper
public interface MemberDao {
	Member getMemberByOnLoginProviderMemberId(@Param("loginProviderTypeCode") String loginProviderTypeCode,
			@Param("onLoginProviderMemberId") int onLoginProviderMemberId);

	void modify(Map<String, Object> param);

	void join(Map<String, Object> param);
}