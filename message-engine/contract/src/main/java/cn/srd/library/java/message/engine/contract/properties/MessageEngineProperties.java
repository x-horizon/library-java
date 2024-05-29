// Copyright (C) 2021-2023 thinkingto.com Ltd. All rights reserved.
// Use of this source code is governed by SRD.
// license that can be found in the LICENSE file.

package cn.srd.library.java.message.engine.contract.properties;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author wjm
 * @since 2024-05-29 22:36
 */
@Getter
@Setter
public class MessageEngineProperties {

    private List<String> serverURLs;

}