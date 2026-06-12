<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Contract;
use Illuminate\Http\Request;
use Stripe\Stripe;
use Stripe\PaymentIntent;
use Stripe\Transfer;

class ContractController extends Controller
{
public function index(Request $request)
{
    $userId = $request->user()->id;
    \Log::info('contracts index userId: ' . $userId);

    $contracts = Contract::with([
        'client:id,name,avatar',
        'freelance:id,name,avatar',
        'jobListing:id,title,category'
    ])
    ->where('client_id', $userId)
    ->orWhere('freelance_id', $userId)
    ->orderBy('created_at', 'desc')
    ->get();

    \Log::info('contracts count: ' . $contracts->count());
    return response()->json($contracts);
}

    public function show(Request $request, $id)
    {
        $userId = $request->user()->id;

        $contract = Contract::with([
            'client:id,name,avatar',
            'freelance:id,name,avatar',
            'jobListing:id,title,category'
        ])
        ->where(function ($q) use ($userId) {
            $q->where('client_id', $userId)
              ->orWhere('freelance_id', $userId);
        })
        ->findOrFail($id);

        return response()->json($contract);
    }

    public function pay(Request $request, $id)
    {
        $contract = Contract::where('client_id', $request->user()->id)
            ->findOrFail($id);

        if ($contract->payment_status !== 'unpaid') {
            return response()->json(['message' => 'Contrat déjà payé'], 422);
        }

        try {
            Stripe::setApiKey(config('services.stripe.secret'));
            $paymentIntent = PaymentIntent::create([
                'amount'   => $contract->amount * 100,
                'currency' => 'eur',
                'metadata' => [
                    'contract_id'  => $contract->id,
                    'client_id'    => $contract->client_id,
                    'freelance_id' => $contract->freelance_id,
                ],
                'capture_method' => 'manual',
            ]);

            $contract->update([
                'payment_status'    => 'escrowed',
                'stripe_payment_id' => $paymentIntent->id,
            ]);

            return response()->json([
                'message'       => 'Paiement escrow créé',
                'client_secret' => $paymentIntent->client_secret,
                'contract'      => $contract
            ]);

        } catch (\Exception $e) {
            return response()->json(['message' => 'Erreur Stripe : ' . $e->getMessage()], 500);
        }
    }

    public function complete(Request $request, $id)
    {
        $contract = Contract::where('freelance_id', $request->user()->id)
            ->findOrFail($id);

        if ($contract->status !== 'active') {
            return response()->json(['message' => 'Contrat non actif'], 422);
        }

        $contract->update(['status' => 'completed']);

        return response()->json([
            'message'  => 'Travail marqué comme terminé',
            'contract' => $contract
        ]);
    }

    public function release(Request $request, $id)
    {
        $contract = Contract::where('client_id', $request->user()->id)
            ->findOrFail($id);

        if ($contract->payment_status !== 'escrowed') {
            return response()->json(['message' => 'Paiement non en escrow'], 422);
        }

        try {
            Stripe::setApiKey(config('services.stripe.secret'));
            $paymentIntent = PaymentIntent::retrieve($contract->stripe_payment_id);
            $paymentIntent->capture();

            $contract->update([
                'payment_status' => 'released',
                'status'         => 'completed',
            ]);

            return response()->json([
                'message'  => 'Paiement libéré au freelance',
                'contract' => $contract
            ]);

        } catch (\Exception $e) {
            return response()->json(['message' => 'Erreur Stripe : ' . $e->getMessage()], 500);
        }
    }

    public function dispute(Request $request, $id)
    {
        $userId = $request->user()->id;

        $contract = Contract::where(function ($q) use ($userId) {
            $q->where('client_id', $userId)
              ->orWhere('freelance_id', $userId);
        })->findOrFail($id);

        $contract->update(['status' => 'disputed']);

        return response()->json([
            'message'  => 'Litige ouvert',
            'contract' => $contract
        ]);
    }

    public function generatePdf(Request $request, $id)
    {
        $userId = $request->user()->id;

        $contract = Contract::with(['client', 'freelance', 'jobListing'])
        ->where(function ($q) use ($userId) {
            $q->where('client_id', $userId)
              ->orWhere('freelance_id', $userId);
        })
        ->findOrFail($id);

        $pdf = app('dompdf.wrapper');
        $pdf->loadView('pdf.contrat', compact('contract'));
        $pdf->setPaper('A4', 'portrait');

        return $pdf->download('contrat-seralink-' . $contract->id . '.pdf');
    }

    public function sign(Request $request, $id)
    {
        $userId = $request->user()->id;
        $userRole = $request->user()->role;

        $contract = Contract::where(function ($q) use ($userId) {
            $q->where('client_id', $userId)
              ->orWhere('freelance_id', $userId);
        })->findOrFail($id);

        if ($userRole === 'client') {
            if ($contract->client_signed) {
                return response()->json(['message' => 'Vous avez déjà signé ce contrat'], 422);
            }
            $contract->update([
                'client_signed'    => true,
                'client_signed_at' => now(),
            ]);
        } else {
            if ($contract->freelance_signed) {
                return response()->json(['message' => 'Vous avez déjà signé ce contrat'], 422);
            }
            $contract->update([
                'freelance_signed'    => true,
                'freelance_signed_at' => now(),
            ]);
        }

        if ($contract->fresh()->client_signed && $contract->fresh()->freelance_signed) {
            $contract->update(['status' => 'active']);
        }

        return response()->json([
            'message'  => 'Contrat signé électroniquement',
            'contract' => $contract->fresh()
        ]);
    }
}