/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.dexecutor.core;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;

import com.github.dexecutor.core.task.ExecutionResult;

public class ExecutionResultTest {
	
	@Test
	public void testExecutionResultIdShouldBeOne() {
		ExecutionResult<Integer, Integer> result = ExecutionResult.success(1, 1);
		assertThat(result.getId(), equalTo(1));
	}
	
	@Test
	public void testExecutionResultToBeOne() {
		ExecutionResult<Integer, Integer> result = ExecutionResult.success(1, 1);
		assertThat(result.getResult(), equalTo(1));
	}

	@Test
	public void testExecutionResultToStringShouldNotBeNull() {
		ExecutionResult<Integer, Integer> result = ExecutionResult.success(1, 1);
		assertNotNull(result.toString());
	}

	@Test
	public void testExecutionResultShouldBeSuccess() {
		ExecutionResult<Integer, Integer> result = ExecutionResult.success(1, 1);
		assertThat(result.isSuccess(), equalTo(true));
	}
	
	@Test
	public void testExecutionResultShouldBeErrored() {
		ExecutionResult<Integer, Integer> result = ExecutionResult.success(1, 1);
		result.errored();
		assertThat(result.isErrored(), equalTo(true));
	}
	
	@Test
	public void testExecutionResultShouldBeSkipped() {
		ExecutionResult<Integer, Integer> result = ExecutionResult.success(1, 1);
		result.skipped();
		assertThat(result.isSkipped(), equalTo(true));
	}

}
