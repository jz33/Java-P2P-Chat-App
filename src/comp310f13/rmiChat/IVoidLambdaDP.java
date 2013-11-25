package comp310f13.rmiChat;

import provided.datapacket.ADataPacket;
import provided.util.IVoidLambda;

/**
 * Convenience interface to get around type-erasure generated warnings about
 * generic types in varargs.
 * @author swong
 */
public interface IVoidLambdaDP extends IVoidLambda<ADataPacket>{

}
