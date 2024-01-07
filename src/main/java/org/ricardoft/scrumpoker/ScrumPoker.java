package org.ricardoft.scrumpoker;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ScrumPoker {

   public List<String> identifyExtremes(List<Estimate> estimates) {

       Estimate lowestEstimate = null;
       Estimate highestEstimate = null;

       if(estimates == null) {
          throw new IllegalArgumentException("Estimates cannot be null");
       }

       if(estimates.size() <= 1) {
           throw new IllegalArgumentException("At least two estimates are required");
       }

       for (Estimate estimate: estimates) {
           if(highestEstimate == null || estimate.getEstimate() > highestEstimate.getEstimate()) {
               highestEstimate = estimate;
           }

           if(lowestEstimate == null || estimate.getEstimate() < lowestEstimate.getEstimate()) {
               lowestEstimate = estimate;
           }

       }

       if(lowestEstimate.equals(highestEstimate))
           return Collections.emptyList();

       return Arrays.asList(
               lowestEstimate.getDeveloper(),
               highestEstimate.getDeveloper()
       );
   }

}
