/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: D:\\GitHub\\TestActivity\\src\\com\\multiprocess\\service\\ITransactionLarge.aidl
 */
package com.multiprocess.service;
public interface ITransactionLarge extends android.os.IInterface
{
    /** Local-side IPC implementation stub class. */
    public static abstract class Stub extends android.os.Binder implements com.multiprocess.service.ITransactionLarge
    {
        private static final java.lang.String DESCRIPTOR = "com.multiprocess.service.ITransactionLarge";
        /** Construct the stub at attach it to the interface. */
        public Stub()
        {
            this.attachInterface(this, DESCRIPTOR);
        }
        /**
         * Cast an IBinder object into an com.multiprocess.service.ITransactionLarge interface,
         * generating a proxy if needed.
         */
        public static com.multiprocess.service.ITransactionLarge asInterface(android.os.IBinder obj)
        {
            if ((obj==null)) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (((iin!=null)&&(iin instanceof com.multiprocess.service.ITransactionLarge))) {
                return ((com.multiprocess.service.ITransactionLarge)iin);
            }
            return new com.multiprocess.service.ITransactionLarge.Stub.Proxy(obj);
        }
        @Override public android.os.IBinder asBinder()
        {
            return this;
        }
        @Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
        {
            switch (code)
            {
                case INTERFACE_TRANSACTION:
                {
                    reply.writeString(DESCRIPTOR);
                    return true;
                }
                case TRANSACTION_transactLargeData:
                {
                    data.enforceInterface(DESCRIPTOR);
                    byte[] _arg0;
                    _arg0 = data.createByteArray();
                    this.transactLargeData(_arg0);
                    return true;
                }
                case TRANSACTION_getTransactLargeData:
                {
                    data.enforceInterface(DESCRIPTOR);
                    byte[] _result = this.getTransactLargeData();
                    reply.writeNoException();
                    reply.writeByteArray(_result);
                    return true;
                }
            }
            return super.onTransact(code, data, reply, flags);
        }
        private static class Proxy implements com.multiprocess.service.ITransactionLarge
        {
            private android.os.IBinder mRemote;
            Proxy(android.os.IBinder remote)
            {
                mRemote = remote;
            }
            @Override public android.os.IBinder asBinder()
            {
                return mRemote;
            }
            public java.lang.String getInterfaceDescriptor()
            {
                return DESCRIPTOR;
            }
            @Override public void transactLargeData(byte[] data) throws android.os.RemoteException
            {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeByteArray(data);
                    mRemote.transact(Stub.TRANSACTION_transactLargeData, _data, null, android.os.IBinder.FLAG_ONEWAY);
                }
                finally {
                    _data.recycle();
                }
            }
            @Override public byte[] getTransactLargeData() throws android.os.RemoteException
            {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                byte[] _result =null;
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    mRemote.transact(Stub.TRANSACTION_getTransactLargeData, _data, _reply, 0);
                    _reply.readException();
                    _result = _reply.createByteArray();
                }/* catch (Exception e) {
                    e.printStackTrace();
                }*/
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }
        }
        static final int TRANSACTION_transactLargeData = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
        static final int TRANSACTION_getTransactLargeData = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
    }
    public void transactLargeData(byte[] data) throws android.os.RemoteException;
    public byte[] getTransactLargeData() throws android.os.RemoteException;
}
