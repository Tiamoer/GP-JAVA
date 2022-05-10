/*
 *	Copyright 2013-2022 Smartdot Technologies Co., Ltd. All rights reserved.
 *	SMARTDOT PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */

package com.yangxy.gpjava.user.service;

import com.yangxy.gpjava.user.entity.SlmUser;

/**
 * <p>
 *
 * @author <a href="mailto:yangxy@smartdot.com.cn">yangxy</a>
 * @version 1.0, 2022/3/13
 */

public interface UserService {
	SlmUser getUserByName(String username);

	SlmUser getUserByPhone(String phone);
}
