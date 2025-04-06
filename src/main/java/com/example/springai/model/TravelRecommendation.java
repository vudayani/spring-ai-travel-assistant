package com.example.springai.model;

import java.util.List;

public record TravelRecommendation(
	    String destination,
	    String bestTimeToVisit,
	    List<String> topAttractions,
	    int estimatedBudget
	) {}
