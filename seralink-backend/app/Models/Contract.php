<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Contract extends Model
{
    use HasFactory;

    protected $fillable = [
        'job_listing_id',
        'client_id',
        'freelance_id',
        'amount',
        'status',
        'payment_status',
        'deadline',
    ];

    public function jobListing()
    {
        return $this->belongsTo(JobListing::class);
    }

    public function client()
    {
        return $this->belongsTo(User::class, 'client_id');
    }

    public function freelance()
    {
        return $this->belongsTo(User::class, 'freelance_id');
    }

    public function messages()
    {
        return $this->hasMany(Message::class);
    }
}