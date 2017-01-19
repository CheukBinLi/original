package java.com.cheuks.bin.original.reflect;

import com.cheuks.bin.original.reflect.rmi.model.TransmissionModel;

public interface InvokeFactory {

	Object invoke(final TransmissionModel transmissionModel) throws Throwable;

}
