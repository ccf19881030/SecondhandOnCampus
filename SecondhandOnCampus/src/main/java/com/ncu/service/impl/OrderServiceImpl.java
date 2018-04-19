package com.ncu.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.ncu.mapper.GoodsMapper;
import com.ncu.mapper.OrderMapper;
import com.ncu.mapper.OrderitemMapper;
import com.ncu.mapper.UserMapper;
import com.ncu.pojo.Cart;
import com.ncu.pojo.CartItem;
import com.ncu.pojo.Goods;
import com.ncu.pojo.Order;
import com.ncu.pojo.OrderExample;
import com.ncu.pojo.Orderitem;
import com.ncu.pojo.OrderitemExample;
import com.ncu.pojo.User;
import com.ncu.pojo.vo.OrderVO;
import com.ncu.pojo.vo.OrderitemVO;
import com.ncu.service.OrderService;

public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    OrderitemMapper orderitemMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    GoodsMapper goodsMapper;
    

    
    @Override
    public boolean save(OrderVO orderVO) {
            orderMapper.insertSelective(orderVO.getOrder());
           for( OrderitemVO itemVO : orderVO.getOrderitemVOs()) {
        	   itemVO.getOrderitem().setOrderId(orderVO.getOrder().getId());
               orderitemMapper.insertSelective(itemVO.getOrderitem());
               System.out.println(itemVO.toString());
           }
           return true;
    }

    @Override
    public List<OrderVO> findOrderByCropId(Integer cropId,Integer orderStatus) {
    	List<OrderVO> orderVOs=new ArrayList<OrderVO>();
        OrderExample ex=new OrderExample();
        ex.createCriteria().andCropIdEqualTo(cropId).andOrderStateEqualTo(orderStatus);
        List<Order> orders = orderMapper.selectByExample(ex);
        OrderitemExample example = new OrderitemExample();
        OrderVO orderVO=null;
        for(Order o : orders){
        	orderVO=new OrderVO();
        	example.createCriteria().andOrderIdEqualTo(o.getId());
        	List<OrderitemVO> items = orderitemMapper.selectByExample(example);
        	for(OrderitemVO itemVO:items){
        		Goods good = goodsMapper.selectByPrimaryKey(itemVO.getOrderitem().getGoodsId());
        		itemVO.setGoodPicPath(good.getPicturePath());
        		itemVO.setGoodCoverPic(good.getCoverPic());
        		itemVO.setGoodName(good.getName());
        	}
        	example.clear();
        	orderVO.setOrder(o);
        	orderVO.setOrderitemVOs(items);
        	orderVOs.add(orderVO);
        	
        }
        return orderVOs;
    }

    @Override
    public List<OrderVO> findOrderByClientId(Integer clientId,Integer orderStatus) {
    	List<OrderVO> orderVOs=new ArrayList<OrderVO>();
        OrderExample ex=new OrderExample();
        if(orderStatus != null){
        	ex.createCriteria().andUserIdEqualTo(clientId).andOrderStateEqualTo(orderStatus);
        }else{
        	ex.createCriteria().andUserIdEqualTo(clientId);
        }
        ex.setOrderByClause("order_date desc");
        List<Order> orders = orderMapper.selectByExample(ex);
        OrderitemExample example = new OrderitemExample();
        OrderVO orderVO=null;
        for(Order o : orders){
        	orderVO=new OrderVO();
        	example.createCriteria().andOrderIdEqualTo(o.getId());
        	List<OrderitemVO> items = orderitemMapper.selectByExample(example);
        	for(OrderitemVO itemVO:items){
        		Goods good = goodsMapper.selectByPrimaryKey(itemVO.getOrderitem().getGoodsId());
        		itemVO.setGoodPicPath(good.getPicturePath());
        		itemVO.setGoodCoverPic(good.getCoverPic());
        		itemVO.setGoodName(good.getName());
        	}
        	example.clear();
        	orderVO.setOrder(o);
        	orderVO.setOrderitemVOs(items);
        	orderVOs.add(orderVO);
        }
        return orderVOs;
    }

    @Override
    public boolean updateOrderStatus(int orderId,int status) {
        OrderExample ex = new OrderExample();
        ex.createCriteria().andOrderStateEqualTo(status);
        Order order = new Order();
        order.setId(orderId);
        orderMapper.updateByExampleSelective(order, ex);
        return true;
    }

	@Override
	public List<OrderVO> separateCartToManyOrder(Cart cart, Integer payway,
			Integer clientUserId) {
        if(cart.getItems().size()==0){return null;}
        Collection<CartItem> items = cart.getItems();
        List<OrderVO> orderVOList= new ArrayList<OrderVO>();

        OrderVO orderVO=new OrderVO();
        Map<Integer,OrderVO> orderMap=new HashMap<Integer,OrderVO>();
        //购物车取出商品项
        for(CartItem item : items){
            //商家id一致的配为一个订单
            if(orderMap.containsKey(item.getGoods().getUserId())){
                orderVO = orderMap.get(item.getGoods().getUserId());
            }else{
                //放入订单  设置订单属性
            	orderVO=new OrderVO();
                Order order =setOrderAttr(item.getGoods().getUserId(),payway,clientUserId);
                //设置商家名
                User crop = userMapper.selectByPrimaryKey(order.getCropId());
            	orderVO.setCropName(crop.getAliasName());
            	
                orderVO.setOrder(order);
                orderMap.put(item.getGoods().getUserId(),orderVO);
            }
            //放入订单项  设置订单项属性
            Orderitem orderitem =setOrderitemAttr(item.getGoods(),item.getQuantity());
            OrderitemVO orderitemVO = new OrderitemVO();
            orderitemVO.setOrderitem(orderitem);
        	//设置商品名
        	Goods good = goodsMapper.selectByPrimaryKey(orderitem.getGoodsId());
        	orderitemVO.setGoodName(good.getName());
            orderitemVO.setGoodUsedMonth(good.getUsedMonth()+"");
            orderitemVO.setGoodCoverPic(good.getCoverPic());
            orderitemVO.setGoodPicPath(good.getPicturePath());
            
            orderVO.getOrderitemVOs().add(orderitemVO);
        }
        //结束后 需要设置订单Order的总价
        setOrderTotalPriceAndPutOrderVOToList(orderMap,orderVOList);

        return orderVOList;
	}

    private void setOrderTotalPriceAndPutOrderVOToList(Map<Integer, OrderVO> orderMap, List<OrderVO> orderVOList) {
        for(Map.Entry<Integer,OrderVO> entry : orderMap.entrySet()){
            double sum=0;
            for(OrderitemVO orderitem   : entry.getValue().getOrderitemVOs()){
                sum+=orderitem.getOrderitem().getPrice();
            }
            entry.getValue().getOrder().setTotalPrice(sum);
            orderVOList.add(entry.getValue());
        }
    }

    private Orderitem setOrderitemAttr(Goods good,Integer quantity) {
        Orderitem orderitem = new Orderitem();
        orderitem.setGoodsId(good.getId());//所属订单ID在插入时设置
        orderitem.setPrice(good.getPrice()*quantity);
        orderitem.setQuantity(quantity);
		return orderitem;
    }

    private Order setOrderAttr(Integer cropId, Integer payway, Integer clientUserId) {
        Order order = new Order();
        order.setCropId(cropId);
        order.setOrderDate(new Date());
        order.setPayWay(payway);
        order.setUserId(clientUserId);
        return order;
    }

	

}
