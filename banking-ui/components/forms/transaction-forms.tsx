"use client";
import { ArrowDownToLine, ArrowUpFromLine, List, FileText } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { ConfirmDialog } from "@/components/confirm-dialog";
import { ResultPanel } from "@/components/result-panel";
import { api, type ApiRes } from "@/lib/api";

type Props = {
  res: Record<string, ApiRes>;
  set: (k: string, v: ApiRes) => void;
  h: () => HeadersInit;
};

export function DepositForm({
  accNum,
  setAccNum,
  txAmount,
  setTxAmount,
  txDesc,
  setTxDesc,
  props,
}: {
  accNum: string;
  setAccNum: (v: string) => void;
  txAmount: string;
  setTxAmount: (v: string) => void;
  txDesc: string;
  setTxDesc: (v: string) => void;
  props: Props;
}) {
  return (
    <>
      <Input
        placeholder="Account Number"
        value={accNum}
        onChange={(e) => setAccNum(e.target.value)}
      />
      <Input
        placeholder="Amount"
        value={txAmount}
        onChange={(e) => setTxAmount(e.target.value)}
      />
      <Input
        placeholder="Description (optional)"
        value={txDesc}
        onChange={(e) => setTxDesc(e.target.value)}
      />
      <ConfirmDialog
        method="POST"
        url="/api/transactions"
        body={{
          accountId: accNum,
          transactionType: "DEPOSIT",
          amount: Number(txAmount),
          description: txDesc || "Deposit",
        }}
        trigger={
          <Button className="w-full bg-green-600 hover:bg-green-700">
            <ArrowDownToLine className="h-4 w-4 mr-2" />
            Deposit
          </Button>
        }
        onConfirm={async () =>
          props.set(
            "deposit",
            await api("/api/transactions", {
              method: "POST",
              headers: props.h(),
              body: JSON.stringify({
                accountId: accNum,
                transactionType: "DEPOSIT",
                amount: Number(txAmount),
                description: txDesc || "Deposit",
              }),
            }),
          )
        }
      />
      <ResultPanel res={props.res.deposit} />
    </>
  );
}

export function WithdrawalForm({
  accNum,
  setAccNum,
  txAmount,
  setTxAmount,
  txDesc,
  setTxDesc,
  props,
}: {
  accNum: string;
  setAccNum: (v: string) => void;
  txAmount: string;
  setTxAmount: (v: string) => void;
  txDesc: string;
  setTxDesc: (v: string) => void;
  props: Props;
}) {
  return (
    <>
      <Input
        placeholder="Account Number"
        value={accNum}
        onChange={(e) => setAccNum(e.target.value)}
      />
      <Input
        placeholder="Amount"
        value={txAmount}
        onChange={(e) => setTxAmount(e.target.value)}
      />
      <Input
        placeholder="Description (optional)"
        value={txDesc}
        onChange={(e) => setTxDesc(e.target.value)}
      />
      <p className="text-xs text-muted-foreground">
        Tài khoản phải ở trạng thái ACTIVE mới rút được.
      </p>
      <ConfirmDialog
        method="POST"
        url="/api/transactions"
        body={{
          accountId: accNum,
          transactionType: "WITHDRAWAL",
          amount: Number(txAmount),
          description: txDesc || "Withdrawal",
        }}
        trigger={
          <Button className="w-full bg-orange-600 hover:bg-orange-700">
            <ArrowUpFromLine className="h-4 w-4 mr-2" />
            Withdraw
          </Button>
        }
        onConfirm={async () =>
          props.set(
            "withdrawal",
            await api("/api/transactions", {
              method: "POST",
              headers: props.h(),
              body: JSON.stringify({
                accountId: accNum,
                transactionType: "WITHDRAWAL",
                amount: Number(txAmount),
                description: txDesc || "Withdrawal",
              }),
            }),
          )
        }
      />
      <ResultPanel res={props.res.withdrawal} />
    </>
  );
}

export function TxByAccForm({
  accNum,
  setAccNum,
  props,
}: {
  accNum: string;
  setAccNum: (v: string) => void;
  props: Props;
}) {
  return (
    <>
      <Input
        placeholder="Account Number"
        value={accNum}
        onChange={(e) => setAccNum(e.target.value)}
      />
      <ConfirmDialog
        method="GET"
        url={`/api/transactions?accountId=${accNum}`}
        trigger={
          <Button className="w-full" variant="outline">
            <List className="h-4 w-4 mr-2" />
            Get Transactions
          </Button>
        }
        onConfirm={async () =>
          props.set(
            "txByAcc",
            await api(`/api/transactions?accountId=${accNum}`, {
              headers: props.h(),
            }),
          )
        }
      />
      <ResultPanel res={props.res.txByAcc} />
    </>
  );
}

export function TxByRefForm({
  refId,
  setRefId,
  props,
}: {
  refId: string;
  setRefId: (v: string) => void;
  props: Props;
}) {
  return (
    <>
      <Input
        placeholder="Reference ID"
        value={refId}
        onChange={(e) => setRefId(e.target.value)}
      />
      <ConfirmDialog
        method="GET"
        url={`/api/transactions/${refId}`}
        trigger={
          <Button className="w-full" variant="outline">
            <FileText className="h-4 w-4 mr-2" />
            Get Transaction
          </Button>
        }
        onConfirm={async () =>
          props.set(
            "txByRef",
            await api(`/api/transactions/${refId}`, { headers: props.h() }),
          )
        }
      />
      <ResultPanel res={props.res.txByRef} />
    </>
  );
}
