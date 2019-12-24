package com.kirin.fm.blog.controller;

import com.kirin.fm.blog.BlogApplicationTests;
import com.kirin.fm.blog.entity.BlogEntity;
import com.kirin.fm.blog.repository.BlogRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Iterator;
import java.util.concurrent.BlockingQueue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BlogApplicationTests.class)
public class IndexControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    protected MockMvc mockMvc;

    protected String id1;

    protected String id2;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        Iterable<BlogEntity> entities = blogRepository.findAll();
        Iterator<BlogEntity> iterator = entities.iterator();
        id1 = iterator.next().getId();
        id2 = iterator.next().getId();
    }

    @Test
    public void test() throws InterruptedException {
        for (int i = 10000; i > 0; i--) {
            Thread thread = new Thread(() -> {
                get(id1);
                get(id2);
            });
            threadPoolTaskExecutor.execute(thread);
        }

        BlockingQueue<Runnable> queue = threadPoolTaskExecutor.getThreadPoolExecutor().getQueue();
        while (queue != null && !queue.isEmpty()) {
            System.out.println("sleep 5 milliseconds to wait, current blocking queue is " + queue.size());
            Thread.sleep(5);
        }
        while (threadPoolTaskExecutor.getActiveCount() > 0) {
            System.out.println("sleep 5 milliseconds to wait, current active count is " + threadPoolTaskExecutor.getActiveCount());
            Thread.sleep(5);
        }
    }

    void get(String id) {
        MockHttpServletRequestBuilder req = MockMvcRequestBuilders.get("/blog/" + id);
        try {
            MvcResult mvcResult = mockMvc.perform(req).andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print()).andReturn();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
