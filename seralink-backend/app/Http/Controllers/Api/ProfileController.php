<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\User;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Hash;

class ProfileController extends Controller
{
    // Mon profil
    public function show(Request $request)
    {
        return response()->json($request->user());
    }

    // Modifier mon profil
public function update(Request $request)
{
    $request->validate([
        'name'  => 'required|string|max:255',
        'email' => 'required|email|unique:users,email,' . $request->user()->id,
    ]);

    $user = $request->user();
    $user->update($request->only(['name', 'email', 'bio']));

    return response()->json($user);
}

    // Profil public d'un freelance
    public function publicProfile($id)
    {
        $user = User::select('id', 'name', 'email', 'role', 'avatar', 'bio', 'skills')
            ->findOrFail($id);

        return response()->json($user);
    }
}