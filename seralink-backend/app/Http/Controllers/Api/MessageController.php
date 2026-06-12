<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Message;
use App\Models\Contract;
use Illuminate\Http\Request;

class MessageController extends Controller
{
    // Liste des messages d'un contrat
public function index(Request $request, $contractId)
{
    $contract = Contract::where(function ($q) use ($request) {
        $q->where('client_id', $request->user()->id)
          ->orWhere('freelance_id', $request->user()->id);
    })->findOrFail($contractId);

    $messages = Message::with('sender:id,name,avatar')
        ->where('contract_id', $contractId)
        ->orderBy('created_at', 'asc')
        ->get()
        ->map(function ($message) use ($request) {
            $message->is_from_me = $message->sender_id === $request->user()->id;
            return $message;
        });

    return response()->json($messages);
}

    // Envoyer un message
    public function store(Request $request, $contractId)
    {
        $request->validate([
            'content' => 'required|string',
        ]);

        $contract = Contract::where(function ($q) use ($request) {
            $q->where('client_id', $request->user()->id)
              ->orWhere('freelance_id', $request->user()->id);
        })->findOrFail($contractId);

        $message = Message::create([
            'contract_id' => $contractId,
            'sender_id'   => $request->user()->id,
            'content'     => $request->content,
        ]);

        $message->load('sender:id,name,avatar');

        return response()->json($message, 201);
    }
}