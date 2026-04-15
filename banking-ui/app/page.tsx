"use client";
import { useState } from "react";
import { Sidebar } from "@/components/sidebar";
import { ApiContent } from "@/components/api-content";
import {
  RegisterForm,
  LoginForm,
  GetUsersForm,
} from "@/components/forms/user-forms";
import {
  CreateAccountForm,
  AccByUserForm,
  AccByNumForm,
  BalanceForm,
  UpdateStatusForm,
  CloseAccountForm,
  AccTxForm,
} from "@/components/forms/account-forms";
import {
  DepositForm,
  WithdrawalForm,
  TxByAccForm,
  TxByRefForm,
} from "@/components/forms/transaction-forms";
import {
  TransferForm,
  TransferDetailForm,
} from "@/components/forms/transfer-forms";
import { authHeader, type ApiRes } from "@/lib/api";
import { nav } from "@/lib/nav";

export default function Home() {
  const [expanded, setExpanded] = useState<Record<string, boolean>>({
    user: true,
  });
  const [active, setActive] = useState("register");
  const [token, setToken] = useState("");
  const [res, setRes] = useState<Record<string, ApiRes>>({});

  const [regName, setRegName] = useState("Test User");
  const [regUser, setRegUser] = useState("admin");
  const [regPass, setRegPass] = useState("123");
  const [loginUser, setLoginUser] = useState("admin");
  const [loginPass, setLoginPass] = useState("123");
  const [accType, setAccType] = useState("SAVINGS_ACCOUNT");
  const [accNum, setAccNum] = useState("0600140000001");
  const [accStatus, setAccStatus] = useState("ACTIVE");
  const [userId, setUserId] = useState("1");
  const [txAmount, setTxAmount] = useState("10000");
  const [txDesc, setTxDesc] = useState("");
  const [fromAcc, setFromAcc] = useState("0600140000001");
  const [toAcc, setToAcc] = useState("0600140000002");
  const [transferAmt, setTransferAmt] = useState("5000");
  const [refId, setRefId] = useState("");

  const set = (k: string, v: ApiRes) => setRes((p) => ({ ...p, [k]: v }));
  const h = () => authHeader(token);
  const toggle = (id: string) => setExpanded((p) => ({ ...p, [id]: !p[id] }));
  const activeItem = nav.flatMap((g) => g.items).find((i) => i.id === active);

  const baseProps = { res, set, token, setToken, h };

  const forms: Record<string, React.ReactNode> = {
    register: (
      <RegisterForm
        state={{
          regName,
          setRegName,
          regUser,
          setRegUser,
          regPass,
          setRegPass,
        }}
        props={baseProps}
      />
    ),
    login: (
      <LoginForm
        state={{ loginUser, setLoginUser, loginPass, setLoginPass }}
        props={baseProps}
      />
    ),
    getUsers: <GetUsersForm props={baseProps} />,
    createAcc: (
      <CreateAccountForm
        accType={accType}
        setAccType={setAccType}
        setAccNum={setAccNum}
        setFromAcc={setFromAcc}
        props={baseProps}
      />
    ),
    accByUser: (
      <AccByUserForm userId={userId} setUserId={setUserId} props={baseProps} />
    ),
    accByNum: (
      <AccByNumForm accNum={accNum} setAccNum={setAccNum} props={baseProps} />
    ),
    balance: (
      <BalanceForm accNum={accNum} setAccNum={setAccNum} props={baseProps} />
    ),
    updateStatus: (
      <UpdateStatusForm
        accNum={accNum}
        setAccNum={setAccNum}
        accStatus={accStatus}
        setAccStatus={setAccStatus}
        props={baseProps}
      />
    ),
    closeAcc: (
      <CloseAccountForm
        accNum={accNum}
        setAccNum={setAccNum}
        props={baseProps}
      />
    ),
    accTx: (
      <AccTxForm accNum={accNum} setAccNum={setAccNum} props={baseProps} />
    ),
    deposit: (
      <DepositForm
        accNum={accNum}
        setAccNum={setAccNum}
        txAmount={txAmount}
        setTxAmount={setTxAmount}
        txDesc={txDesc}
        setTxDesc={setTxDesc}
        props={baseProps}
      />
    ),
    withdrawal: (
      <WithdrawalForm
        accNum={accNum}
        setAccNum={setAccNum}
        txAmount={txAmount}
        setTxAmount={setTxAmount}
        txDesc={txDesc}
        setTxDesc={setTxDesc}
        props={baseProps}
      />
    ),
    txByAcc: (
      <TxByAccForm accNum={accNum} setAccNum={setAccNum} props={baseProps} />
    ),
    txByRef: (
      <TxByRefForm refId={refId} setRefId={setRefId} props={baseProps} />
    ),
    transfer: (
      <TransferForm
        fromAcc={fromAcc}
        setFromAcc={setFromAcc}
        toAcc={toAcc}
        setToAcc={setToAcc}
        transferAmt={transferAmt}
        setTransferAmt={setTransferAmt}
        setRefId={setRefId}
        props={baseProps}
      />
    ),
    transferDetail: (
      <TransferDetailForm refId={refId} setRefId={setRefId} props={baseProps} />
    ),
  };

  return (
    <div className="flex h-screen bg-background">
      <Sidebar
        active={active}
        expanded={expanded}
        token={token}
        onSelect={setActive}
        onToggle={toggle}
        onLogout={() => setToken("")}
      />
      <main className="flex-1 overflow-auto p-8">
        <div className="max-w-2xl mx-auto">
          {activeItem && (
            <ApiContent item={activeItem}>{forms[active]}</ApiContent>
          )}
        </div>
      </main>
    </div>
  );
}
