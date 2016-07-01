/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: IStopPackageService.aidl
 */
package com.multiprocess.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public interface IStopPackageService extends android.os.IInterface {
    /** Local-side IPC implementation stub class. */
    public static abstract class Stub extends android.os.Binder implements
            com.multiprocess.service.IStopPackageService {
        private static final java.lang.String DESCRIPTOR = "com.multiprocess.service.IStopPackageService";

        /** Construct the stub at attach it to the interface. */
        public Stub() {
            this.attachInterface(this, DESCRIPTOR);
        }

        /**
         * Cast an IBinder object into an
         * com.multiprocess.service.IStopPackageService interface, generating a
         * proxy if needed.
         */
        public static com.multiprocess.service.IStopPackageService asInterface(
                android.os.IBinder obj) {
            if ((obj == null)) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (((iin != null) && (iin instanceof com.multiprocess.service.IStopPackageService))) {
                return ((com.multiprocess.service.IStopPackageService) iin);
            }
            return new com.multiprocess.service.IStopPackageService.Stub.Proxy(
                    obj);
        }

        @Override
        public android.os.IBinder asBinder() {
            return this;
        }

        @Override
        public boolean onTransact(int code, android.os.Parcel data,
                android.os.Parcel reply, int flags)
                throws android.os.RemoteException {
            try {
                switch (code) {
                case INTERFACE_TRANSACTION: {
                    reply.writeString(DESCRIPTOR);
                    return true;
                }
                case TRANSACTION_killNoWait: {
                    data.enforceInterface(DESCRIPTOR);
                    java.lang.String _arg0;
                    _arg0 = data.readString();
                    this.killNoWait(_arg0);
                    return true;
                }
                case TRANSACTION_killWait: {
                    data.enforceInterface(DESCRIPTOR);
                    java.lang.String _arg0;
                    _arg0 = data.readString();
                    this.killWait(_arg0);
                    reply.writeNoException();
                    return true;
                }
                case TRANSACTION_killAllNoWait: {
                    data.enforceInterface(DESCRIPTOR);
                    java.lang.String[] _arg0;
                    _arg0 = data.createStringArray();
                    this.killAllNoWait(_arg0);
                    return true;
                }
                case TRANSACTION_killAllWait: {
                    data.enforceInterface(DESCRIPTOR);
                    java.lang.String[] _arg0;
                    _arg0 = data.createStringArray();
                    this.killAllWait(_arg0);
                    reply.writeNoException();
                    return true;
                }
                case TRANSACTION_killSysNoWait: {
                    data.enforceInterface(DESCRIPTOR);
                    this.killSysNoWait();
                    return true;
                }
                case TRANSACTION_killSysWait: {
                    data.enforceInterface(DESCRIPTOR);
                    this.killSysWait();
                    reply.writeNoException();
                    return true;
                }
                case TRANSACTION_killUserNoWait: {
                    data.enforceInterface(DESCRIPTOR);
                    this.killUserNoWait();
                    return true;
                }
                }
            } catch (RuntimeException e) {
                reply.setDataPosition(0);
                reply.writeInt(-3);
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);
                String stack = sw.toString();
                try {
                    sw.close();
                    pw.close();
                } catch (IOException e1) {
                }
                reply.writeString(stack);
                return true;
            }

            return super.onTransact(code, data, reply, flags);
        }

        private static class Proxy implements
                com.multiprocess.service.IStopPackageService {
            private android.os.IBinder mRemote;

            Proxy(android.os.IBinder remote) {
                mRemote = remote;
            }

            @Override
            public android.os.IBinder asBinder() {
                return mRemote;
            }

            public java.lang.String getInterfaceDescriptor() {
                return DESCRIPTOR;
            }

            @Override
            public void killNoWait(java.lang.String name)
                    throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(name);
                    mRemote.transact(Stub.TRANSACTION_killNoWait, _data, null,
                            android.os.IBinder.FLAG_ONEWAY);
                } finally {
                    _data.recycle();
                }
            }

            @Override
            public void killWait(java.lang.String name)
                    throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(name);
                    mRemote.transact(Stub.TRANSACTION_killWait, _data, _reply,
                            0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override
            public void killAllNoWait(java.lang.String[] names)
                    throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeStringArray(names);
                    mRemote.transact(Stub.TRANSACTION_killAllNoWait, _data,
                            null, android.os.IBinder.FLAG_ONEWAY);
                } finally {
                    _data.recycle();
                }
            }

            @Override
            public void killAllWait(java.lang.String[] names)
                    throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeStringArray(names);
                    mRemote.transact(Stub.TRANSACTION_killAllWait, _data,
                            _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override
            public void killSysNoWait() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    mRemote.transact(Stub.TRANSACTION_killSysNoWait, _data,
                            null, android.os.IBinder.FLAG_ONEWAY);
                } finally {
                    _data.recycle();
                }
            }

            @Override
            public void killSysWait() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    mRemote.transact(Stub.TRANSACTION_killSysWait, _data,
                            _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override
            public void killUserNoWait() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    mRemote.transact(Stub.TRANSACTION_killUserNoWait, _data,
                            null, android.os.IBinder.FLAG_ONEWAY);
                } finally {
                    _data.recycle();
                }
            }
        }

        static final int TRANSACTION_killNoWait = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
        static final int TRANSACTION_killWait = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
        static final int TRANSACTION_killAllNoWait = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
        static final int TRANSACTION_killAllWait = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
        static final int TRANSACTION_killSysNoWait = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
        static final int TRANSACTION_killSysWait = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
        static final int TRANSACTION_killUserNoWait = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
    }

    public void killNoWait(java.lang.String name)
            throws android.os.RemoteException;

    public void killWait(java.lang.String name)
            throws android.os.RemoteException;

    public void killAllNoWait(java.lang.String[] names)
            throws android.os.RemoteException;

    public void killAllWait(java.lang.String[] names)
            throws android.os.RemoteException;

    public void killSysNoWait() throws android.os.RemoteException;

    public void killSysWait() throws android.os.RemoteException;

    public void killUserNoWait() throws android.os.RemoteException;
}
