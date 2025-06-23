package com.tharindu.web.rest;

import jakarta.ws.rs.*;

import java.sql.SQLOutput;

@Path("/")
public class Home {

    //JAX-RS(Specification)
    //Jersey,RESTEasy

    @GET
    public String home(){
        System.out.println("Home");
        return "This is My Home Page";
    }

    @GET
    @Path("user")
    public String hello(){
        System.out.println("accessing user page");
        return "This is My User Page";
    }
}

