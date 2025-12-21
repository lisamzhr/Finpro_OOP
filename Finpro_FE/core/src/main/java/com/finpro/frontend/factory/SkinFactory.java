// factories/SkinFactory.java
package com.finpro.frontend.factory;

import com.finpro.frontend.models.*;

public class SkinFactory {

    public static Skin createSkin(int skinId) {
        switch(skinId) {
            case 0: return new CasualSkin();
            case 1: return new SportSkin();
            case 2: return new FormalSkin();
            case 3: return new ModernSkin();
            case 4: return new TraditionalSkin();
            case 5: return new ElegantSkin();
            default: return new CasualSkin(); // fallback
        }
    }

    public static int getTotalSkins() {
        return 6;
    }
}
