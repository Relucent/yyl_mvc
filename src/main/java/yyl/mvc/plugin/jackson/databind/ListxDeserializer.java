package yyl.mvc.plugin.jackson.databind;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import yyl.mvc.common.collection.Listx;

/**
 * _Listx反序列化
 */

public class ListxDeserializer extends JsonDeserializer<Listx> {

    /** Singleton instance to use. */
    public final static ListxDeserializer INSTANCE = new ListxDeserializer();

    @Override
    public Listx deserialize(JsonParser parser, DeserializationContext context)
            throws IOException, JsonProcessingException {
        try {
            TreeNode node = parser.readValueAsTree();
            return TreeNodeConverts.toListx(node);
        } catch (Exception e) {
            return null;
        }
    }
}
