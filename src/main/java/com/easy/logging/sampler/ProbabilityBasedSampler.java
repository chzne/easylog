package com.easy.logging.sampler;

import com.easy.logging.Sampler;

public class ProbabilityBasedSampler implements Sampler {
    @Override
    public boolean isSampled(long id) {
        return false;
    }
}
