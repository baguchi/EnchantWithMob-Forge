package baguchi.enchantwithmob.utils;

import com.mojang.logging.LogUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;
import org.slf4j.Logger;

public class EntityUtils {
    private static final Logger LOGGER = LogUtils.getLogger();

    public static void loadFrom(Entity owner, Entity target) {
        try (ProblemReporter.ScopedCollector problemreporter$scopedcollector = new ProblemReporter.ScopedCollector(owner.problemPath(), LOGGER)) {
            TagValueOutput tagvalueoutput = TagValueOutput.createWithContext(problemreporter$scopedcollector, owner.registryAccess());
            target.saveWithoutId(tagvalueoutput);
            CompoundTag compoundtag = tagvalueoutput.buildResult();
            owner.load(TagValueInput.create(problemreporter$scopedcollector, owner.registryAccess(), compoundtag));
        }
    }
}
