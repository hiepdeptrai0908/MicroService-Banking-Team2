"use client";
import { Send, Eye } from "lucide-react";
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

export function TransferForm({
  fromAcc,
  setFromAcc,
  toAcc,
  setToAcc,
  transferAmt,
  setTransferAmt,
  setRefId,
  props,
}: {
  fromAcc: string;
  setFromAcc: (v: string) => void;
  toAcc: string;
  setToAcc: (v: string) => void;
  transferAmt: string;
  setTransferAmt: (v: string) => void;
  setRefId: (v: string) => void;
  props: Props;
}) {
  return (
    <>
      <Input
        placeholder="From Account"
        value={fromAcc}
        onChange={(e) => setFromAcc(e.target.value)}
      />
      <Input
        placeholder="To Account"
        value={toAcc}
        onChange={(e) => setToAcc(e.target.value)}
      />
      <Input
        placeholder="Amount"
        value={transferAmt}
        onChange={(e) => setTransferAmt(e.target.value)}
      />
      <ConfirmDialog
        method="POST"
        url="/api/fund-transfers"
        body={{ fromAccount: fromAcc, toAccount: toAcc, amount: transferAmt }}
        trigger={
          <Button className="w-full">
            <Send className="h-4 w-4 mr-2" />
            Transfer
          </Button>
        }
        onConfirm={async () => {
          const r = await api("/api/fund-transfers", {
            method: "POST",
            headers: props.h(),
            body: JSON.stringify({
              fromAccount: fromAcc,
              toAccount: toAcc,
              amount: transferAmt,
            }),
          });
          props.set("transfer", r);
          if (
            r &&
            r.status < 400 &&
            typeof r.data === "object" &&
            r.data !== null &&
            "transactionReference" in r.data
          )
            setRefId(
              (r.data as { transactionReference: string }).transactionReference,
            );
        }}
      />
      <ResultPanel res={props.res.transfer} />
    </>
  );
}

export function TransferDetailForm({
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
        url={`/api/fund-transfers/${refId}`}
        trigger={
          <Button className="w-full" variant="outline">
            <Eye className="h-4 w-4 mr-2" />
            Get Detail
          </Button>
        }
        onConfirm={async () =>
          props.set(
            "transferDetail",
            await api(`/api/fund-transfers/${refId}`, { headers: props.h() }),
          )
        }
      />
      <ResultPanel res={props.res.transferDetail} />
    </>
  );
}
