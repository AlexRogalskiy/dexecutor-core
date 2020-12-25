package com.github.dexecutor.core;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;

import com.github.dexecutor.core.graph.Node;
import com.github.dexecutor.core.support.TestUtil;
import com.github.dexecutor.core.support.ThreadPoolUtil;
import com.github.dexecutor.core.task.Task;
import com.github.dexecutor.core.task.TaskProvider;

public class DexecutorRecoveringTest {
	
	@Test
	public void testRecover() {
		DefaultDexecutor<Integer, Integer> executor = newDexecutor();

		executor.recoverExecution(ExecutionConfig.NON_TERMINATING);
		Collection<Node<Integer, Integer>> processedNodesOrder = TestUtil.processedNodesOrder(executor);
		assertThat(processedNodesOrder).containsAll(executionOrderExpectedResult());
		assertThat(processedNodesOrder).size().isEqualTo(14);	
	}
	
	@Test(expected = IllegalStateException.class)
	public void testRecoverTerminated() {
		DefaultDexecutor<Integer, Integer> executor = newDexecutor();

		executor.execute(ExecutionConfig.NON_TERMINATING);		
		executor.recoverExecution(ExecutionConfig.NON_TERMINATING);		
	}

	private DefaultDexecutor<Integer, Integer> newDexecutor() {
		ExecutorService executorService = newExecutor();
		DexecutorConfig<Integer, Integer> config = new DexecutorConfig<>(executorService, new SleepyTaskProvider());
		DexecutorState<Integer, Integer> state = new DefaultDexecutorState<>();
		config.setDexecutorState(state);
		DefaultDexecutor<Integer, Integer> executor = new DefaultDexecutor<Integer, Integer>(config);

		executor.addDependency(1, 2);
		executor.addDependency(1, 2);
		executor.addDependency(1, 3);
		executor.addDependency(3, 4);
		executor.addDependency(3, 5);
		executor.addDependency(3, 6);
		executor.addDependency(2, 7);
		executor.addDependency(2, 9);
		executor.addDependency(2, 8);
		executor.addDependency(9, 10);
		executor.addDependency(12, 13);
		executor.addDependency(13, 4);
		executor.addDependency(13, 14);
		executor.addIndependent(11);
		return executor;
	}

	private Collection<Node<Integer, Integer>> executionOrderExpectedResult() {
		List<Node<Integer, Integer>> result = new ArrayList<Node<Integer, Integer>>();
		result.add(new Node<Integer, Integer>(1));
		result.add(new Node<Integer, Integer>(11));
		result.add(new Node<Integer, Integer>(12));
		result.add(new Node<Integer, Integer>(3));
		result.add(new Node<Integer, Integer>(2));
		result.add(new Node<Integer, Integer>(7));
		result.add(new Node<Integer, Integer>(9));
		result.add(new Node<Integer, Integer>(10));
		result.add(new Node<Integer, Integer>(8));
		result.add(new Node<Integer, Integer>(13));
		result.add(new Node<Integer, Integer>(5));
		result.add(new Node<Integer, Integer>(6));
		result.add(new Node<Integer, Integer>(4));
		result.add(new Node<Integer, Integer>(14));
		return result;
	}

	private ExecutorService newExecutor() {
		return Executors.newFixedThreadPool(ThreadPoolUtil.ioIntesivePoolSize());
	}

	private static class SleepyTaskProvider implements TaskProvider<Integer, Integer> {
		
		public Task<Integer, Integer> provideTask(final Integer id) {

			return new Task<Integer, Integer>() {

				private static final long serialVersionUID = 1L;

				public Integer execute() {
					System.out.println("Result : " + this.getResult(id - 1));		
					return id;
				}
			};			
		}		
	}
}
