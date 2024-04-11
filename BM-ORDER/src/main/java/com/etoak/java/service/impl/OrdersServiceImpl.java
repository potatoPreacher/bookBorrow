package com.etoak.java.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.etoak.java.entity.Orders;
import com.etoak.java.mapper.OrdersMapper;
import com.etoak.java.service.IOrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;


@Service
@RefreshScope                       // 变量改变时刷新相关实体类
public class OrdersServiceImpl
        extends ServiceImpl<OrdersMapper, Orders>
        implements IOrdersService {

    @Autowired
    OrdersMapper ordersMapper;

    @Value("${order.header}")
    private String orderNoHeader;     //前缀 nacos作用


    /**
     * 添加订单
     * @param order
     * @return
     */
    @Override
    public int addOrders(Orders order) {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 随机生成书籍
        Random rand = new Random();
        String bookNo = "B" + formatter.format(date) + new String(String.valueOf(rand.nextInt(9000)+1000));

        // 随机生成订单号
        String orderNo = orderNoHeader + formatter.format(date);
        order.setOrderNo(orderNo);

        // 标记创建时间
        order.setCreateTime(date);

        int result = ordersMapper.insert(order);
        return result;
    }

    /**
     * 根据id删除订单
     * @param id
     * @return
     */
    @Override
    public int deleteById(Integer id) {
        int result = ordersMapper.deleteById(id);
        return result;
    }

    /**
     * 通过订单号查询订单
     * @param orderNo
     * @return
     */
    @Override
    public Orders selectByNo(String orderNo) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.like("order_no", orderNo);
        Orders result = ordersMapper.selectOne(wrapper);
        return result;
    }

    /**
     * 多条件筛选订单
     *
     * @param order
     * @return
     */
    @Override
    public List listOrders(Orders order) {
        QueryWrapper wrapper = new QueryWrapper();
        if(order.getOrderNo()!=null && !order.getOrderNo().isEmpty()){
            wrapper.like("order_no", order.getOrderNo());
        }
        if(order.getCreateUser()!=null && !order.getCreateUser().isEmpty()){
            wrapper.like("create_user", order.getCreateUser());
        }
        if(order.getCreateTime()!=null ){
            wrapper.like("create_time", order.getCreateTime());
        }
        if(order.getBookName()!=null && !order.getBookName().isEmpty()){
            wrapper.like("book_name", order.getBookName());
        }
        if(order.getPublisher()!=null && !order.getPublisher().isEmpty()){
            wrapper.like("publisher", order.getPublisher());
        }
        if(order.getPublishTime()!=null ){
            wrapper.like("publish_time", order.getPublishTime());
        }
        if(order.getAuthor()!=null && !order.getAuthor().isEmpty()){
            wrapper.like("author", order.getAuthor());
        }
        if(order.getBookLable()!=null && !order.getBookLable().isEmpty()){
            wrapper.like("book_lable", order.getBookLable());
        }
        if(order.getBookNumbers() != null){
            wrapper.like("book_numbers", order.getBookNumbers());
        }
        if(order.getTotalPrice()!=null ){
            wrapper.like("total_price", order.getTotalPrice());
        }
        if(order.getStatus() != null){
            wrapper.eq("status", order.getStatus());
        }
        if(order.getComment()!=null && !order.getComment().isEmpty()){
            wrapper.like("comment", order.getComment());
        }
        if(order.getApprovalTime()!=null ){
            wrapper.like("approval_time", order.getApprovalTime());
        }
        return ordersMapper.selectList(wrapper);
    }

    /**
     * 更新order
     * @param order
     * @return
     */
    @Override
    public int updateOrders(Orders order) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.setEntity(order);
        int result = ordersMapper.update(wrapper);
        return result;
    }

    /**
     * 更新order的status为1 通过
     * @param orderNo
     * @return
     */
    @Override
    public int approved(String orderNo) {
        int result = ordersMapper.updateOrderStatus(orderNo, 1);
        return result;
    }

    /**
     * 更新order的status为2 拒绝
     * @param orderNo
     * @return
     */
    @Override
    public int rejected(String orderNo) {
        int result = ordersMapper.updateOrderStatus(orderNo, 2);
        return result;
    }

    /**
     * 根据年份计算总的花费
     * @param yearNeed
     * @return
     */
    @Override
    public BigDecimal computeByYear(Integer yearNeed) {
//        System.out.println(yearNeed);
        BigDecimal result = ordersMapper.computeByYear(yearNeed);
        return result;
    }

    /**
     * 根据出版社计算总的花费
     * @param publisher
     * @return
     */
    @Override
    public BigDecimal computeByPublisher(String publisher) {
        BigDecimal result = ordersMapper.computeByPublisher(publisher);
        return result;
    }
}
