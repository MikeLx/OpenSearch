/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * The OpenSearch Contributors require contributions made to
 * this file be licensed under the Apache-2.0 license or a
 * compatible open source license.
 */

/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

/*
 * Modifications Copyright OpenSearch Contributors. See
 * GitHub history for details.
 */

package org.opensearch.index.mapper;

import org.opensearch.Version;
import org.opensearch.cluster.metadata.IndexMetadata;
import org.opensearch.common.settings.Settings;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

public class IpRangeFieldTypeTests extends FieldTypeTestCase {

    public void testFetchSourceValue() throws IOException {
        Settings settings = Settings.builder().put(IndexMetadata.SETTING_VERSION_CREATED, Version.CURRENT.id).build();
        Mapper.BuilderContext context = new Mapper.BuilderContext(settings, new ContentPath());

        RangeFieldMapper mapper = new RangeFieldMapper.Builder("field", RangeType.IP, true).build(context);
        Map<String, Object> range = org.opensearch.common.collect.Map.of("gte", "2001:db8:0:0:0:0:2:1");
        assertEquals(
            Collections.singletonList(org.opensearch.common.collect.Map.of("gte", "2001:db8::2:1")),
            fetchSourceValue(mapper.fieldType(), range)
        );
        assertEquals(Collections.singletonList("2001:db8::2:1/32"), fetchSourceValue(mapper.fieldType(), "2001:db8:0:0:0:0:2:1/32"));
    }
}
