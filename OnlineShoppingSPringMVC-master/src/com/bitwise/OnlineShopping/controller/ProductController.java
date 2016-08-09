package com.bitwise.OnlineShopping.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import com.bitwise.OnlineShopping.model.CartOperations;
import com.bitwise.OnlineShopping.model.ProductInfo;
import com.bitwise.OnlineShopping.model.Products;

@Controller
public class ProductController {
	
	@Autowired
	CartOperations cartOperations;
	
	
	@RequestMapping(value="/productList",method=RequestMethod.GET)
	public ModelAndView showProducts(Model model,@ModelAttribute("products") Products productsbean,
            BindingResult result,HttpServletRequest request, 
            HttpServletResponse response) {
		Products products = new Products();
		
		HttpSession session=request.getSession(false);  
             if(session==null || (session!=null && session.isNew()))
            	 return new ModelAndView("redirect:/");
		@SuppressWarnings("unchecked")
		Map<String, ProductInfo> mapcart=(Map<String, ProductInfo>) session.getAttribute("map");
		
		products=mapSession(mapcart);
		ModelAndView mv = new ModelAndView("Products");
		mv.addObject("products", products);
		Map<String, ProductInfo> mapcontroller=products.getMap1();
		 model.addAttribute("map1",mapcontroller);
		 model.addAttribute("list",products.getList());
		return mv;
		
	}
	
	
	
	private Products mapSession(Map<String, ProductInfo> mapcontroller)
	{
		Products p=new Products();
		if(mapcontroller!=null)
		{
		p.setList(new ArrayList(mapcontroller.keySet()));
		p.setMap1(mapcontroller);
		}
		return p;
	}
	
	@RequestMapping(value="/addtoCart",method=RequestMethod.POST)
	public String addToCart(Model model,Products products,
            BindingResult result,HttpServletRequest request, 
            HttpServletResponse response){
		System.out.println("in post of product controller add to cart");
		model.addAttribute("list",products.getList());
		cartOperations.addToCart(products.getList());
		Map<String, ProductInfo> mapcart=cartOperations.getCartmap();
		model.addAttribute("map",mapcart);
		model.addAttribute("list", products.getList());
		 HttpSession session=request.getSession();  
	        session.setAttribute("map",mapcart); 
	       
		return "DisplayProducts";
	}
	
	@RequestMapping(value="/deleteFromCart" ,method=RequestMethod.POST)
	public String againAddtoCart(Model model,Products products,
            BindingResult result,HttpServletRequest request, 
            HttpServletResponse response)
	{	
		
		HttpSession session=request.getSession();  
        
		@SuppressWarnings("unchecked")
		Map<String, ProductInfo> mapcart=(Map<String, ProductInfo>) session.getAttribute("map");
		
		
		model.addAttribute("list",products.getList());
		cartOperations.deleteFromCart(products.getList(),mapcart);
		Map<String, ProductInfo> mapcart1=cartOperations.getCartmap();
		
		session.setAttribute("map",mapcart1);
		
		model.addAttribute("map",mapcart1);
		model.addAttribute("list", products.getList());
		return "DisplayProducts";
	}
	
	
}